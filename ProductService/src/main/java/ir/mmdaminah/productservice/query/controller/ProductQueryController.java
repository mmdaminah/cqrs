package ir.mmdaminah.productservice.query.controller;

import ir.mmdaminah.productservice.core.dto.ProductDto;
import ir.mmdaminah.productservice.query.queries.FindProductQuery;
import ir.mmdaminah.productservice.query.queries.FindProductsQuery;
import jakarta.validation.constraints.NotBlank;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<ProductDto> getProducts() {

        // TODO: it is throwing error and not working.
        return queryGateway.query(
                new FindProductsQuery(),
                ResponseTypes.multipleInstancesOf(ProductDto.class)
        ).join();
    }

    @GetMapping("/{id}")
    public ProductDto findProduct(@PathVariable @NotBlank String id) {
        return queryGateway.query(
                new FindProductQuery(id),
                ResponseTypes.instanceOf(ProductDto.class)
        ).join();
    }


}
