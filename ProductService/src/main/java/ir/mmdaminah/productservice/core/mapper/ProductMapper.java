package ir.mmdaminah.productservice.core.mapper;

import ir.mmdaminah.productservice.command.aggregate.ProductAggregate;
import ir.mmdaminah.productservice.core.data.Product;
import ir.mmdaminah.productservice.events.ProductCreatedEvent;
import ir.mmdaminah.productservice.events.ProductUpdatedEvent;
import org.mapstruct.*;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {
    @Mapping(target = "productId", ignore = true)
    void updateProductFromProductUpdatedEvent(ProductUpdatedEvent event, @MappingTarget Product entity);

    @Mapping(target= "title")
    @Mapping(target = "productId", ignore = true)
    void updateFromEvent(ProductCreatedEvent event, @MappingTarget Product product);

}
