package ir.mmdaminah.productservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLookupRepository extends JpaRepository<ProductLookup, String> {

    ProductLookup findByProductIdOrTitle(String id, String title);

}
