package h2go.repository;

import h2go.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByIdAndDeletedAtIsNull(String id);

    Page<Order> findByUserIdAndDeletedAtIsNull(String userId, Pageable pageable);


    Page<Order> findByProviderIdAndDeletedAtIsNull(String providerId, Pageable pageable);


    Page<Order> findByDeletedAtIsNull(Pageable pageable);
}
