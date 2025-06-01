package ir.mmdaminah.productservice.command.interceptors;

import ir.mmdaminah.productservice.command.commands.CreateProductCommand;
import ir.mmdaminah.productservice.core.data.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if (CreateProductCommand.class.equals(command.getPayloadType())) {

                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                var productLookup = productLookupRepository.findByProductIdOrTitle(
                        createProductCommand.getProductId(),
                        createProductCommand.getTitle()
                );

                if (productLookup != null) {
                    throw new IllegalStateException("Product with productId %s or title %s exists"
                            .formatted(createProductCommand.getProductId(), createProductCommand.getTitle())
                    );
                }

            }
            return command;
        };
    }
}
