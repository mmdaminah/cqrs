package ir.mmdaminah.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReservedEvent {
    private String orderId;
    private String productId;
    private Integer quantity;
    private String userId;

}
