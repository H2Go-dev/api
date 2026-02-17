package h2go.repository;

import h2go.dto.response.ProductResponse;
import h2go.model.Address;
import h2go.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findAllByOrderByVolumeAsc(Pageable pageable);
    Optional<Product> findByIdAndDeletedAtIsNull(String id);
}
