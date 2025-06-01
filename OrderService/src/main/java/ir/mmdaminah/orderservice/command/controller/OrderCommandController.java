package ir.mmdaminah.orderservice.command.controller;

import ir.mmdaminah.orderservice.command.commands.CreateOrderCommand;
import ir.mmdaminah.orderservice.core.data.OrderStatus;
import ir.mmdaminah.orderservice.core.dto.CreateOrderDto;
import ir.mmdaminah.orderservice.core.dto.OrderDto;
import ir.mmdaminah.orderservice.query.queries.FindOrderQuery;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public OrderCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping()
    public OrderDto createOrder(@Valid @RequestBody CreateOrderDto createOrderDto) {
        String orderId = UUID.randomUUID().toString();
        String userId = "27b9582 9-4f3f-4ddf-8983-151ba010e35b";


        SubscriptionQueryResult<OrderDto, OrderDto> queryResult = queryGateway.subscriptionQuery(
                new FindOrderQuery(orderId),
                ResponseTypes.instanceOf(OrderDto.class),
                ResponseTypes.instanceOf(OrderDto.class)
        );

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .addressId(createOrderDto.getAddressId())
                .productId(createOrderDto.getProductId())
                .userId(userId)
                .quantity(createOrderDto.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .build();

        try {
            commandGateway.sendAndWait(createOrderCommand);
            return queryResult.updates().blockFirst();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            queryResult.close();
        }

    }

}
