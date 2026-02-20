package h2go.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApproveOrderRequest(
        @NotNull(message = "approved can't be null")
        Boolean approved,
        String rejectionReason,
        @Future(message = "Delivery date must be in the future")
        LocalDateTime deliveryDate

) {
}
