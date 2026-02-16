package h2go.model;

import h2go.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus =  SubscriptionStatus.PENDING;

    private String rejectionReason = null;

    private LocalDateTime startDate = LocalDateTime.now();

    private LocalDateTime endDate = null;

    private LocalDateTime deletedAt = null;

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void endSubscription() {
        this.endDate = LocalDateTime.now();
    }
}
