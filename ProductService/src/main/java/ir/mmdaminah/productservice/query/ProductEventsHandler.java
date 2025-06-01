package ir.mmdaminah.productservice.query;

import ir.mmdaminah.core.events.ProductReservationCanceledEvent;
import ir.mmdaminah.core.events.ProductReservedEvent;
import ir.mmdaminah.productservice.core.data.Product;
import ir.mmdaminah.productservice.core.data.ProductRepository;
import ir.mmdaminah.productservice.core.dto.ProductDto;
import ir.mmdaminah.productservice.events.ProductCreatedEvent;
import ir.mmdaminah.productservice.events.ProductDeletedEvent;
import ir.mmdaminah.productservice.events.ProductUpdatedEvent;
import ir.mmdaminah.productservice.core.mapper.ProductMapper;
import ir.mmdaminah.productservice.query.queries.FindProductQuery;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private QueryUpdateEmitter queryUpdateEmitter;

    Logger logger = LoggerFactory.getLogger(ProductEventsHandler.class);

    public ProductEventsHandler(ProductRepository productRepository, ProductMapper productMapper, QueryUpdateEmitter queryUpdateEmitter) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {

        Product product = new Product();
        BeanUtils.copyProperties(event, product);

        productRepository.save(product);

        queryUpdateEmitter.emit(
                FindProductQuery.class,
                query -> true,
                new ProductDto(
                        event.getProductId(),
                        event.getTitle(),
                        event.getQuantity(),
                        event.getPrice()
                )
        );


        logger.info("ProductCreatedEvent: productId={}, title={}, quantity={}, price={}",
                event.getProductId(),
                event.getTitle(),
                event.getQuantity(),
                event.getPrice()
        );
    }

    @EventHandler
    public void on(ProductUpdatedEvent event) {

        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Product with Id %s not found".formatted(event.getProductId())));

        productMapper.updateProductFromProductUpdatedEvent(event, product);

        productRepository.save(product);

        logger.info("ProductUpdatedEvent: productId={}, title={}, quantity={}, price={}",
                event.getProductId(),
                event.getTitle(),
                event.getQuantity(),
                event.getPrice()
        );
    }

    @EventHandler
    public void on(ProductDeletedEvent event) {
        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Product with Id %s not found".formatted(event.getProductId())));
        productRepository.delete(product);
        logger.info("ProductDeletedEvent: orderId={}", event.getProductId());
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        Product product = productRepository.findByProductId(event.getProductId()).orElseThrow(
                () -> new RuntimeException("Product with Id %s not found".formatted(event.getProductId()))
        );
        product.setQuantity(product.getQuantity() - event.getQuantity());
        productRepository.save(product);

        logger.info("ProductReservedEvent: orderId={}", event.getProductId());
    }


    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException e) {
        // Log error message
        throw e;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception e) throws Exception {
        // Log error message
        throw e;
    }

    @EventHandler
    public void on(ProductReservationCanceledEvent event){
        Optional<Product> product = productRepository.findById(event.getProductId());
        product.ifPresent(value -> {
            value.setQuantity(value.getQuantity() + event.getQuantity());
            productRepository.save(value);
        });
    }

}
