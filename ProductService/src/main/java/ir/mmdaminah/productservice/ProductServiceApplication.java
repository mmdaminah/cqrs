package ir.mmdaminah.productservice;

import ir.mmdaminah.productservice.command.interceptors.CreateProductCommandInterceptor;
import ir.mmdaminah.productservice.core.exception.ProductServiceEventsExceptionHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    // it was working without this configuration
    @Autowired
    public void registerCreateProductCommandInterceptor(
            ApplicationContext context, CommandBus commandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler(
                "product-group",
                conf -> new ProductServiceEventsExceptionHandler()
        );
    }
}



