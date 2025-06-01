package ir.mmdaminah.orderservice.query.queries;

import lombok.Data;
import lombok.Value;

@Value
public class FindOrderQuery {

    private final String orderId;

}
