package ir.mmdaminah.paymentservice.query;

import ir.mmdaminah.core.events.PaymentProcessedEvent;
import ir.mmdaminah.paymentservice.data.Payment;
import ir.mmdaminah.paymentservice.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventsHandler {

    private final PaymentRepository paymentRepository;

    public PaymentEventsHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {

        paymentRepository.save(
                Payment.builder()
                        .paymentId(event.getPaymentId())
                        .orderId(event.getOrderId())
                        .build()
        );

    }

}
