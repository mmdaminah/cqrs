package ir.mmdaminah.productservice.command.aggregate;

import ir.mmdaminah.core.commands.CancelProductReservationCommand;
import ir.mmdaminah.core.commands.ReserveProductCommand;
import ir.mmdaminah.core.events.ProductReservationCanceledEvent;
import ir.mmdaminah.core.events.ProductReservedEvent;
import ir.mmdaminah.productservice.command.commands.CreateProductCommand;
import ir.mmdaminah.productservice.command.commands.DeleteProductCommand;
import ir.mmdaminah.productservice.command.commands.UpdateProductCommand;
import ir.mmdaminah.productservice.events.ProductCreatedEvent;
import ir.mmdaminah.productservice.events.ProductDeletedEvent;
import ir.mmdaminah.productservice.events.ProductUpdatedEvent;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

@Aggregate
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private Integer quantity;
    private Integer price;

    Logger logger = LoggerFactory.getLogger(ProductAggregate.class);

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        logger.info("CreateProductCommand: productId={}, title={}, quantity={}, price={}",
                command.getProductId(),
                command.getTitle(),
                command.getQuantity(),
                command.getPrice()
        );

        ProductCreatedEvent event = new ProductCreatedEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.title = event.getTitle();
        this.quantity = event.getQuantity();
        this.price = event.getPrice();
    }

    @CommandHandler
    public void handle(UpdateProductCommand command) {
        logger.info("UpdateProductCommand: productId={}, title={}, quantity={}, price={}",
                command.getProductId(),
                command.getTitle(),
                command.getQuantity(),
                command.getPrice()
        );

        ProductUpdatedEvent event = new ProductUpdatedEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductUpdatedEvent event) {
        if (event.getTitle() != null) {
            title = event.getTitle();
        }
        if (event.getQuantity() != null) {
            quantity = event.getQuantity();
        }
        if (event.getPrice() != null) {
            price = event.getPrice();
        }
    }

    @CommandHandler
    public void handle(DeleteProductCommand command) {
        logger.info("ProductDeletedCommand: productId={}", command.getId());
        ProductDeletedEvent event = new ProductDeletedEvent(command.getId());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductDeletedEvent event) {
    }

    @CommandHandler
    public void handle(ReserveProductCommand command) {
        logger.info("ReserveProductCommand: orderId={}, productId={}, quantity={}, userId={}",
                command.getOrderId(),
                command.getProductId(),
                command.getQuantity(),
                command.getUserId()
        );
        if (quantity < command.getQuantity()) {
            // TODO: this exception is not being handled in the ProductServiceExceptionHandler
            throw new IllegalStateException("Reserve product quantity is less than requested");
        }

        ProductReservedEvent event = new ProductReservedEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent event) {
        this.quantity -= event.getQuantity();
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand command){
        logger.info("CancelProductReservationCommand: productId={}, orderId={}, userId={}, quantity={}, reason={}",
                command.getProductId(),
                command.getOrderId(),
                command.getUserId(),
                command.getQuantity(),
                command.getReason()
        );
        ProductReservationCanceledEvent event = new ProductReservationCanceledEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductReservationCanceledEvent event) {
        this.quantity += event.getQuantity();
    }

}
