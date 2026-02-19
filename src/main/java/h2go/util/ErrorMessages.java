package h2go.util;

public final class ErrorMessages {

    public static final String USER_NOT_FOUND = "User not found";
    public static final String PROVIDER_NOT_FOUND = "Provider not found";
    public static final String PROVIDER_NOT_APPROVED = "Provider is not approved";
    public static final String PROVIDER_PENDING = "Provider is still pending approval";
    public static final String SUBSCRIPTION_NOT_FOUND = "Subscription not found";
    public static final String SUBSCRIPTION_NOT_APPROVED = "Subscription is not approved";
    public static final String SUBSCRIPTION_ALREADY_PROCESSED = "Subscription has already been processed";
    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String ORDER_ALREADY_APPROVED = "Order has already been approved";
    public static final String ORDER_REJECTED = "Order is rejected";
    public static final String ORDER_PENDING = "Order is still pending approval";
    public static final String ADDRESS_NOT_FOUND = "Address not found";
    public static final String ADDRESS_NOT_OWNED = "Address does not belong to user";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_NOT_AVAILABLE = "Product is not available from this provider";
    public static final String INSUFFICIENT_STOCK = "Insufficient stock";
    public static final String INVALID_QUANTITY = "Quantity must be greater than zero";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String UNAUTHORIZED_ACTION = "You are not authorized to perform this action";
    public static final String INVALID_PAGE_PARAMS = "Invalid pagination parameters";

    private ErrorMessages() {
    }
}
