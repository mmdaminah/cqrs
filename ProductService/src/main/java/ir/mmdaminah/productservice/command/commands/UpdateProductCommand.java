package ir.mmdaminah.productservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCommand {
    @TargetAggregateIdentifier
    private String productId;
    private String title;
    private Integer quantity;
    private Integer price;
}
