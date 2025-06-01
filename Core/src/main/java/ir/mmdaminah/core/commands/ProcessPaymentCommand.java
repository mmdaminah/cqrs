package ir.mmdaminah.core.commands;

import ir.mmdaminah.core.data.PaymentDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPaymentCommand {

    private String paymentId;
    private String orderId;
    private PaymentDetails paymentDetails;

}
