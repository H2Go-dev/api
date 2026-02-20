package h2go.repository;

import h2go.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByDeletedAtIsNull(Pageable pageable);

    Optional<Product> findByIdAndDeletedAtIsNull(String id);

    Page<Product> findByProviderIdAndDeletedAtIsNull(String providerId, Pageable pageable);

    Page<Product> findByProviderUserEmailAndDeletedAtIsNull(String email, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Product> findByIdWithLock(@Param("id") String id);
}
