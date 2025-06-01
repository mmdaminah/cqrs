package ir.mmdaminah.orderservice.command.aggregate;

import ir.mmdaminah.orderservice.command.commands.ApproveOrderCommand;
import ir.mmdaminah.orderservice.command.commands.CreateOrderCommand;
import ir.mmdaminah.orderservice.command.commands.RejectOrderCommand;
import ir.mmdaminah.orderservice.core.data.OrderStatus;
import ir.mmdaminah.orderservice.events.OrderApprovedEvent;
import ir.mmdaminah.orderservice.events.OrderCreatedEvent;
import ir.mmdaminah.orderservice.events.OrderRejectedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String userId;
    private String productId;
    private Integer quantity;
    private String addressId;
    private OrderStatus orderStatus;

    Logger logger = LoggerFactory.getLogger(OrderAggregate.class);

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        logger.info("CreateOrderCommand: orderId={}, userId={}, productId={}, quantity={}, addressId={}, orderStatus={}",
                command.getOrderId(),
                command.getUserId(),
                command.getProductId(),
                command.getQuantity(),
                command.getAddressId(),
                command.getOrderStatus()
        );

        OrderCreatedEvent event = new OrderCreatedEvent();

        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
        this.addressId = event.getAddressId();
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand command) {
        logger.info("ApproveOrderCommand: orderId={}", orderId);

        OrderApprovedEvent event = new OrderApprovedEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(RejectOrderCommand command) {
        logger.info("RejectOrderCommand: orderId={}, reason={}",
                command.getOrderId(),
                command.getReason()
        );
        OrderRejectedEvent event = new OrderRejectedEvent(
                command.getOrderId(),
                command.getReason()
        );

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent event){
        orderStatus = event.getOrderStatus();
    }

}
