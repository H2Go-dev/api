package h2go.repository;

import h2go.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByIdAndDeletedAtIsNull(String id);

    List<Order> findByUserIdAndDeletedAtIsNull(String userId);

    List<Order> findByProviderIdAndDeletedAtIsNull(String providerId);

    List<Order> findByDeletedAtIsNull();
}
