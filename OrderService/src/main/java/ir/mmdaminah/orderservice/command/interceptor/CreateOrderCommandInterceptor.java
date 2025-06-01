package ir.mmdaminah.orderservice.command.interceptor;

import ir.mmdaminah.orderservice.command.commands.CreateOrderCommand;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

public class CreateOrderCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index,  command) -> {
            if(CreateOrderCommand.class.equals(command.getPayloadType())){

                CreateOrderCommand createOrderCommand = (CreateOrderCommand) command.getPayload();

                // Do nothing for now

            }
            return command;
        };
    }
}
