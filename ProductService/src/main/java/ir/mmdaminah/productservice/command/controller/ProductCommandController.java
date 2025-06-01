package ir.mmdaminah.productservice.command.controller;

import ir.mmdaminah.productservice.command.commands.CreateProductCommand;
import ir.mmdaminah.productservice.command.commands.DeleteProductCommand;
import ir.mmdaminah.productservice.command.commands.UpdateProductCommand;
import ir.mmdaminah.productservice.core.dto.CreateProductDto;
import ir.mmdaminah.productservice.core.dto.ProductDto;
import ir.mmdaminah.productservice.core.dto.UpdateProductDto;
import ir.mmdaminah.productservice.query.queries.FindProductQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    @Autowired
    CommandGateway commandGateway;
    @Autowired
    QueryGateway queryGateway;

    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody CreateProductDto createProductDto) {
        String productId = UUID.randomUUID().toString();

        SubscriptionQueryResult<ProductDto, ProductDto> queryResult = queryGateway
                .subscriptionQuery(
                        new FindProductQuery(productId),
                        ResponseTypes.instanceOf(ProductDto.class),
                        ResponseTypes.instanceOf(ProductDto.class)
                );

        CreateProductCommand command = CreateProductCommand.builder()
                .productId(productId)
                .title(createProductDto.getTitle())
                .quantity(createProductDto.getQuantity())
                .price(createProductDto.getPrice())
                .build();

        try {
            commandGateway.sendAndWait(command);
            return queryResult.updates().blockFirst();
        }
        finally {
            queryResult.close();
        }
    }

    @PutMapping
    public String updateProduct(@Valid @RequestBody UpdateProductDto updateProductDto) {

        UpdateProductCommand updateProductCommand = new UpdateProductCommand();

        BeanUtils.copyProperties(updateProductDto, updateProductCommand);

        return commandGateway.sendAndWait(updateProductCommand);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable @NotNull @NotBlank String id) {

        DeleteProductCommand deleteProductCommand = new DeleteProductCommand(id);

        return commandGateway.sendAndWait(deleteProductCommand);
    }

}
