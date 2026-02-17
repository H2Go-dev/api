package h2go.dto.request;

import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.UUID;

public record OrderItemDTO(
        @UUID
        String productId,
        @Min(value = 1, message = "quantity can't be zero or less")
        Integer quantity
) {
}
