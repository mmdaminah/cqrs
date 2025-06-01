package ir.mmdaminah.productservice.core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProductDto {

    @NotNull
    private String productId;

    private String title;

    @Min(value = 0)
    private Integer quantity;

    @Min(value = 0)
    private Integer price;

}
