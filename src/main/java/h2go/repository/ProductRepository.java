package h2go.repository;

import h2go.dto.response.ProductResponse;
import h2go.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<ProductResponse> findAllByOrderByVolumeAsc(Pageable pageable);
}
