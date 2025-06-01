package ir.mmdaminah.productservice.query;

import ir.mmdaminah.productservice.core.data.ProductLookup;
import ir.mmdaminah.productservice.core.data.ProductLookupRepository;
import ir.mmdaminah.productservice.events.ProductCreatedEvent;
import ir.mmdaminah.productservice.events.ProductDeletedEvent;
import ir.mmdaminah.productservice.events.ProductUpdatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;
    Logger logger = LoggerFactory.getLogger(ProductLookupEventsHandler.class);

    public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductLookup productLookup = new ProductLookup(event.getProductId(), event.getTitle());
        productLookupRepository.save(productLookup);
        logger.info("ProductCreatedEvent:ProductLookupCreated: productId={}, title={}",
                event.getProductId(),
                event.getTitle()
        );
    }

    @EventHandler
    public void on(ProductDeletedEvent event) {
        productLookupRepository.deleteById(event.getProductId());
        logger.info("ProductDeletedEvent:ProductLookupDeleted: productId={}", event.getProductId());
    }

    @EventHandler
    public void on(ProductUpdatedEvent event) {
        var productLookup = productLookupRepository.findById(event.getProductId()).orElseThrow(
                () -> new RuntimeException("Product with id %s not found".formatted(event.getProductId()))
        );
        productLookup.setTitle(event.getTitle());
        productLookupRepository.save(productLookup);
        logger.info("ProductUpdatedEvent:ProductLookupUpdated: productId={}, title={}",
                event.getProductId(),
                event.getTitle()
        );
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception e) throws Exception {
        // Log error message
        throw e;
    }

}


