package ir.mmdaminah.orderservice.core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @NotEmpty
    private String productId;
    @Min(1)
    private Integer quantity;
    @NotEmpty
    private String addressId;
}
