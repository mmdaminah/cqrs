package ir.mmdaminah.productservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProductDeletedEvent {
    private String productId;
}
