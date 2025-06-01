package ir.mmdaminah.orderservice.events;

import ir.mmdaminah.orderservice.core.data.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    public String orderId;
    private String userId;
    private String productId;
    private Integer quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
