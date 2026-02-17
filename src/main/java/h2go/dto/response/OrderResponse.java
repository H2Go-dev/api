package h2go.dto.response;

import h2go.model.enums.OrderStatus;
import h2go.model.enums.OrderType;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        String id,
        OrderStatus orderStatus,
        OrderType orderType,
        Double totalPrice,
        String rejectionReason,
        LocalDateTime deliveryDate,
        String userId,
        String userName,
        String providerId,
        String providerBusinessName,
        List<OrderItemResponse> orderItems
) {
}
