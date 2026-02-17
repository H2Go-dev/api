package h2go.repository;

import h2go.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    Optional<Subscription> findByIdAndDeletedAtIsNull(String id);

    Optional<Subscription> findByUserIdAndProviderId(String userId, String providerId);
}
