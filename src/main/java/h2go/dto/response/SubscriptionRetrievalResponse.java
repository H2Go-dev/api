package h2go.dto.response;

import h2go.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

public record SubscriptionRetrievalResponse(
        String id,
        UserRetrievalResponse user,
        ProviderSummaryResponse provider,
        SubscriptionStatus subscriptionStatus,
        String rejectionReason,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime deletedAt
) {
}
