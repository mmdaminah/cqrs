package ir.mmdaminah.orderservice.core.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @Column(unique = true)
    private String orderId;
    private String userId;
    private Integer quantity;
    private String addressId;
    private String productId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
