package ir.mmdaminah.productservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAll();

    Optional<Product> findByProductId(String id);

}
