package h2go.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import h2go.model.enums.OrderType;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record OrderCreationRequest(
        @NotNull(message = "providerId is required")
        @UUID
        String providerId,
        @NotNull(message = "addressId is required")
        @UUID
        String addressId,
        @Valid
        @NotEmpty(message = "products cannot be empty")
        List<OrderItemDTO> products,
        @NotNull(message = "orderType is required")
        OrderType orderType
) {
}
