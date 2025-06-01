package ir.mmdaminah.core.commands;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CancelProductReservationCommand {
    @TargetAggregateIdentifier
    private String productId;
    private String orderId;
    private String userId;
    private Integer quantity;
    private String reason;
}
