package h2go.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApproveOrderRequest(
        @NotNull(message = "approved can't be null")
        Boolean approved,
        String rejectionReason,
        LocalDateTime deliveryDate

) {
}
