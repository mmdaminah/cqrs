package ir.mmdaminah.core.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveProductCommand {

    @TargetAggregateIdentifier
    private String productId;
    private String orderId;
    private Integer quantity;
    private String userId;

}
