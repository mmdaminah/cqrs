package ir.mmdaminah.productservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@Data
public class DeleteProductCommand {
    @TargetAggregateIdentifier
    private String id;
}
