package h2go.dto.request;

import h2go.model.enums.OrderStatus;

public record ChangeOrderStatusRequest(
        OrderStatus orderStatus
) {
}
