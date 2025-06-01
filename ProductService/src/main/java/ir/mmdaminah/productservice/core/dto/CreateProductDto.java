package ir.mmdaminah.productservice.core.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CreateProductDto {

    private String title;

    @Min(value = 0)
    private Integer quantity = 0;

    @Min(value = 0)
    private Integer price = 0;

}
