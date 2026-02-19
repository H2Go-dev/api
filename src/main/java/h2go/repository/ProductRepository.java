package h2go.repository;

import h2go.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByDeletedAtIsNull(Pageable pageable);

    Optional<Product> findByIdAndDeletedAtIsNull(String id);

    Page<Product> findByProviderIdAndDeletedAtIsNull(String providerId, Pageable pageable);

    Page<Product> findByProviderUserEmailAndDeletedAtIsNull(String email, Pageable pageable);
}
