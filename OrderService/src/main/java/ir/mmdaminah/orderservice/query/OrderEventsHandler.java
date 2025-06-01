package ir.mmdaminah.orderservice.query;

import ir.mmdaminah.orderservice.core.data.Order;
import ir.mmdaminah.orderservice.core.data.OrderRepository;
import ir.mmdaminah.orderservice.core.data.OrderStatus;
import ir.mmdaminah.orderservice.events.OrderApprovedEvent;
import ir.mmdaminah.orderservice.events.OrderCreatedEvent;
import ir.mmdaminah.orderservice.events.OrderRejectedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    private final OrderRepository orderRepository;
    Logger logger = LoggerFactory.getLogger(OrderEventsHandler.class);


    public OrderEventsHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        Order order = new Order();
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
        logger.info("OrderCreateEvent: orderId={}, userId={}, productId={}, quantity={}, addressId={}, orderStatus={}",
                event.getOrderId(),
                event.getUserId(),
                event.getProductId(),
                event.getQuantity(),
                event.getAddressId(),
                event.getOrderStatus()
        );
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow(null);
        order.setOrderStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
        logger.info("OrderApprovedEvent: orderId={}, orderStatus={}",
                event.getOrderId(),
                order.getOrderStatus()
        );
    }

    @EventHandler
    public void on(OrderRejectedEvent event){
        Optional<Order> order = orderRepository.findById(event.getOrderId());
        order.ifPresent(order1 -> {
            order1.setOrderStatus(event.getOrderStatus());
            orderRepository.save(order1);
        });
    }

}
