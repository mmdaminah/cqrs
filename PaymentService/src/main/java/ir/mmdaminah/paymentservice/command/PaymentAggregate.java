package ir.mmdaminah.paymentservice.command;

import ir.mmdaminah.core.commands.ProcessPaymentCommand;
import ir.mmdaminah.core.events.PaymentProcessedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;

    Logger logger = LoggerFactory.getLogger(PaymentAggregate.class);

    public PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command) {
        logger.info("ProcessPaymentCommand: paymentId={}, orderId={}",
                command.getPaymentId(),
                command.getOrderId()
        );

        PaymentProcessedEvent event = new PaymentProcessedEvent(
                command.getPaymentId(),
                command.getOrderId()
        );

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }

}
