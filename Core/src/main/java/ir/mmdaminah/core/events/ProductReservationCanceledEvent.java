package ir.mmdaminah.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReservationCanceledEvent {
    private String productId;
    private String orderId;
    private String userId;
    private Integer quantity;
    private String reason;
}
