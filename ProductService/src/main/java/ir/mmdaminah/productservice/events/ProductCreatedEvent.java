package ir.mmdaminah.productservice.events;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCreatedEvent {
    private String productId;
    private String title;
    private Integer quantity;
    private Integer price;
}
