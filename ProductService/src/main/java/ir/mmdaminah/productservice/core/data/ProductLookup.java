package ir.mmdaminah.productservice.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "product-lookups")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductLookup {
    @Id
    @Column(unique = true)
    private String productId;
    @Column(unique = true)
    private String title;
}
