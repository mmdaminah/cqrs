package ir.mmdaminah.orderservice.events;

import ir.mmdaminah.orderservice.core.data.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    private String orderId;
    private String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
