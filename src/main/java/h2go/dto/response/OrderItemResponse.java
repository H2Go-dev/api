package h2go.dto.response;


public record OrderItemResponse(
        String id,
        String productName,
        Integer quantity,
        Double priceAtPurchase
) {
}
