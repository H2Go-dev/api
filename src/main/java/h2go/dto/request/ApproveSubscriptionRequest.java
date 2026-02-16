package h2go.dto.request;

import jakarta.validation.constraints.NotNull;

public record ApproveSubscriptionRequest(
        @NotNull(message = "approved can't be null")
        Boolean approved,
        String rejectionReason
) {
}
