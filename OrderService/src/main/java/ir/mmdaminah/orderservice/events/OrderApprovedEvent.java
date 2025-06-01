package ir.mmdaminah.orderservice.events;

import ir.mmdaminah.orderservice.core.data.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderApprovedEvent {
    private String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
