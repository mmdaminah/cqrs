package ir.mmdaminah.orderservice.saga;

import ir.mmdaminah.core.commands.CancelProductReservationCommand;
import ir.mmdaminah.core.commands.ProcessPaymentCommand;
import ir.mmdaminah.core.commands.ReserveProductCommand;
import ir.mmdaminah.core.data.User;
import ir.mmdaminah.core.events.PaymentProcessedEvent;
import ir.mmdaminah.core.events.ProductReservationCanceledEvent;
import ir.mmdaminah.core.events.ProductReservedEvent;
import ir.mmdaminah.core.query.GetUserDetailsQuery;
import ir.mmdaminah.orderservice.command.commands.ApproveOrderCommand;
import ir.mmdaminah.orderservice.command.commands.RejectOrderCommand;
import ir.mmdaminah.orderservice.core.dto.OrderDto;
import ir.mmdaminah.orderservice.events.OrderApprovedEvent;
import ir.mmdaminah.orderservice.events.OrderCreatedEvent;
import ir.mmdaminah.orderservice.events.OrderRejectedEvent;
import ir.mmdaminah.orderservice.query.queries.FindOrderQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Saga
public class OrderSaga {

    public static final String PAYMENT_PROCESSING_DEADLINE = "payment-processing-deadline";
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;
    @Autowired
    private transient DeadlineManager deadlineManager;

    private String deadlineId;

    transient Logger logger = LoggerFactory.getLogger(OrderSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        logger.info("OrderSaga:OrderCratedEvent: orderId={}, productId={}, userId={}, quantity={}",
                event.getOrderId(),
                event.getProductId(),
                event.getUserId(),
                event.getQuantity()
        );

        var reserveProductCommand = ReserveProductCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .userId(event.getUserId())
                .quantity(event.getQuantity())
                .build();

        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // Start a compensating transaction
            } else {
                logger.info("OrderSaga:OrderCreatedEvent: reserve product command handled");
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event) {

        User user = null;

        try {
            user = queryGateway.query(
                    new GetUserDetailsQuery(event.getUserId()),
                    ResponseTypes.instanceOf(User.class)
            ).join();
        } catch (Exception e) {
            cancelProductReservation(event, e.getMessage());
            return;
        }

        if (user == null) {
            cancelProductReservation(event, "User not found");
            return;
        }

        deadlineId = deadlineManager.schedule(
                Duration.of(10, ChronoUnit.SECONDS),
                PAYMENT_PROCESSING_DEADLINE,
                event
        );

        if (true) return;

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(event.getOrderId())
                .paymentDetails(user.getPaymentDetails())
                .build();
        String result;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand);
        } catch (Exception e) {
            cancelProductReservation(event, e.getMessage());
            return;
        }

        if (result == null) {
            cancelProductReservation(event, "Could not process payment with provided data");
            return;
        }

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent event) {
        cancelDeadline();
        commandGateway.send(new ApproveOrderCommand(event.getOrderId()));
    }

    private void cancelDeadline() {
        if (deadlineId != null) {
            deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, deadlineId);
            deadlineId = null;
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent event) {
        logger.info("ApproveOrderCommand: orderId={}", event.getOrderId());
        queryUpdateEmitter.emit(
                FindOrderQuery.class,
                query -> true,
                new OrderDto(event.getOrderId(), event.getOrderStatus())
        );
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCanceledEvent event) {
        RejectOrderCommand command = new RejectOrderCommand(
                event.getOrderId(),
                event.getReason()
        );

        commandGateway.send(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent event) {
        logger.info("OrderSaga:OrderRejectedEvent: orderId={}", event.getOrderId());
    }

    private void cancelProductReservation(ProductReservedEvent event, String reason) {
        cancelDeadline();
        CancelProductReservationCommand command = CancelProductReservationCommand
                .builder()
                .orderId(event.getProductId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(command);

    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
    public void handlePaymentDeadline(ProductReservedEvent event) {
        // TODO: when this deadline happens the request stays in pending state, it should be closed.
        logger.info("OrderSaga: Payment deadline happened for orderId={}, productId={}, userId={}, quantity={}",
                event.getOrderId(),
                event.getProductId(),
                event.getUserId(),
                event.getQuantity()
        );
        cancelProductReservation(event, "Payment timeout");

    }

}
