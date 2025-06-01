package ir.mmdaminah.orderservice.command.commands;

import ir.mmdaminah.orderservice.core.data.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private Integer quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
