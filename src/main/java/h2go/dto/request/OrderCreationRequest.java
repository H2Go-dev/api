package h2go.dto.request;

import h2go.model.enums.OrderType;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record OrderCreationRequest(
        @UUID
        String providerId,
        @UUID
        String addressId,
        List<OrderItemDTO> products,
        OrderType orderType
) {
}
