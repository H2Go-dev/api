package h2go.repository;

import h2go.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    Optional<OrderItem> findByIdAndDeletedAtIsNull(String id);

}
