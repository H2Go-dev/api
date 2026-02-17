package h2go.mapper;

import h2go.dto.response.OrderItemResponse;
import h2go.dto.response.OrderResponse;
import h2go.model.Order;
import h2go.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "userId", source = "order.user.id")
    @Mapping(target = "userName", source = "order.user.name")
    @Mapping(target = "providerId", source = "order.provider.id")
    @Mapping(target = "providerBusinessName", source = "order.provider.businessName")
    @Mapping(target = "orderItems", source = "order.orderItems")
    OrderResponse toDto(Order order);

    default OrderItemResponse orderItemToDto(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getPriceAtPurchase()
        );
    }

    default List<OrderItemResponse> orderItemsToDtoList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::orderItemToDto)
                .toList();
    }
}
