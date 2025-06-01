package ir.mmdaminah.orderservice.core.dto;

import ir.mmdaminah.orderservice.core.data.OrderStatus;
import lombok.Value;

@Value
public class OrderDto {

    private final String orderId;
    private final OrderStatus orderStatus;

}
