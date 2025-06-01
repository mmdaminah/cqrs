package ir.mmdaminah.productservice.query.queries;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
public class FindProductQuery {
    private String productId;
}
