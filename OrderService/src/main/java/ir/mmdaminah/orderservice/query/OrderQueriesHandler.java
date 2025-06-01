package ir.mmdaminah.orderservice.query;

import ir.mmdaminah.orderservice.core.data.Order;
import ir.mmdaminah.orderservice.core.data.OrderRepository;
import ir.mmdaminah.orderservice.core.dto.OrderDto;
import ir.mmdaminah.orderservice.query.queries.FindOrderQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderQueriesHandler {

    private final OrderRepository orderRepository;

    public OrderQueriesHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @QueryHandler
    public OrderDto findOrder(FindOrderQuery query) {
        Order order = orderRepository.findByOrderId(query.getOrderId()).orElseThrow(
                () -> new RuntimeException("Order with id=%s not found".formatted(query.getOrderId()))
        );
        return new OrderDto(order.getOrderId(), order.getOrderStatus());
    }

}
