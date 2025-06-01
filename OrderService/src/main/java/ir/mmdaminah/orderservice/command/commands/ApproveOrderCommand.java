package ir.mmdaminah.orderservice.command.commands;

import ir.mmdaminah.orderservice.core.data.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
}
