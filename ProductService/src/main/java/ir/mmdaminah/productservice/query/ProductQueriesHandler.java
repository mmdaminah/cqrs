package ir.mmdaminah.productservice.query;

import ir.mmdaminah.productservice.core.data.Product;
import ir.mmdaminah.productservice.core.data.ProductRepository;
import ir.mmdaminah.productservice.core.dto.ProductDto;
import ir.mmdaminah.productservice.query.queries.FindProductQuery;
import ir.mmdaminah.productservice.query.queries.FindProductsQuery;
import jakarta.persistence.EntityNotFoundException;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class ProductQueriesHandler {

    private final ProductRepository productRepository;
    Logger logger = Logger.getLogger(ProductQueriesHandler.class.getName());

    public ProductQueriesHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public ProductDto findProduct(FindProductQuery findProductQuery) {

        Product product = productRepository.findByProductId(findProductQuery.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found %s".formatted(findProductQuery.getProductId()))
                );

        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);

        return productDto;
    }

    @QueryHandler
    public List<ProductDto> findAllProducts(FindProductsQuery query) {
        return productRepository.findAll().stream()
                .map(product -> new ProductDto(
                        product.getProductId(),
                        product.getTitle(),
                        product.getQuantity(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
//        List<ProductDto> productDtos = new ArrayList<>();
//        List<Product> products = productRepository.findAll();
//
//        for (Product product : products) {
//            ProductDto productDto = new ProductDto();
//            BeanUtils.copyProperties(product, productDto);
//            productDtos.add(productDto);
//        }
//
//        return productDtos;
    }


}

