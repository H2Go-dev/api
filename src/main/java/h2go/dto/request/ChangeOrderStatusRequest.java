package h2go.dto.request;

import jakarta.validation.constraints.NotNull;
import h2go.model.enums.OrderStatus;

public record ChangeOrderStatusRequest(
        @NotNull(message = "orderStatus is required")
        OrderStatus orderStatus
) {
}
