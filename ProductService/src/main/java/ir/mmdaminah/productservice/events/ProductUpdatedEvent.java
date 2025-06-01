package ir.mmdaminah.productservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdatedEvent {
    @TargetAggregateIdentifier
    private String productId;
    private String title;
    private Integer quantity;
    private Integer price;
}
