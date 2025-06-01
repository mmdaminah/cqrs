package ir.mmdaminah.productservice.core.data;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "products")
@Entity
@Data
public class Product {
    @Id
    @Column(unique = true)
    private String productId;

    @Column(unique = true)
    private String title;

    private Integer quantity;

    private Integer price;
}
