package h2go.service;

import h2go.dto.request.ApproveOrderRequest;
import h2go.dto.request.ChangeOrderStatusRequest;
import h2go.dto.request.OrderCreationRequest;
import h2go.dto.response.OrderResponse;
import h2go.exception.ApiException;
import h2go.mapper.OrderMapper;
import h2go.model.Address;
import h2go.model.Order;
import h2go.model.OrderItem;
import h2go.model.Product;
import h2go.model.Provider;
import h2go.model.Subscription;
import h2go.model.User;
import h2go.model.enums.OrderStatus;
import h2go.model.enums.RegistrationStatus;
import h2go.model.enums.Role;
import h2go.model.enums.SubscriptionStatus;
import h2go.repository.AddressRepository;
import h2go.repository.OrderItemRepository;
import h2go.repository.OrderRepository;
import h2go.repository.ProductRepository;
import h2go.repository.ProviderRepository;
import h2go.repository.SubscriptionRepository;
import h2go.repository.UserRepository;
import h2go.util.ErrorMessages;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderItemRepository orderItemRepository;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProviderRepository providerRepository;

    private final AddressRepository addressRepository;

    private final ProductRepository productRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final OrderMapper orderMapper;

    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String createOrder(String email, OrderCreationRequest orderCreationRequest) {
        User user =  userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.UNAUTHORIZED));

        Provider provider = providerRepository.findByIdAndDeletedAtIsNull(orderCreationRequest.providerId())
                .orElseThrow(() -> new ApiException("Provider not found", HttpStatus.NOT_FOUND));

        Address address = addressRepository.findByIdAndDeletedAtIsNull(orderCreationRequest.addressId())
                .orElseThrow(() -> new ApiException("Address not found", HttpStatus.NOT_FOUND));


        if (!provider.getRegistrationStatus().equals(RegistrationStatus.APPROVED)) {
            throw new ApiException("provider is not approved", HttpStatus.BAD_REQUEST);
        }

        if (!address.getUser().getEmail().equals(user.getEmail())) {
            throw new ApiException("address doesn't exist with user", HttpStatus.BAD_REQUEST);
        }

        Subscription subscription = subscriptionRepository.findByUserIdAndProviderId(user.getId(), provider.getId())
                .orElseThrow(() -> new ApiException("User not subscribed to provider", HttpStatus.NOT_FOUND));

        if (!subscription.getSubscriptionStatus().equals(SubscriptionStatus.APPROVED)) {
            throw new  ApiException("subscription is not approved", HttpStatus.BAD_REQUEST);
        }

        List<Product> productsToUpdate = new ArrayList<>();

        List<OrderItem> orderItemList = orderCreationRequest.products().stream().map(
                (e) -> {

                    Product product = productRepository.findByIdAndDeletedAtIsNull(e.productId())
                            .orElseThrow(() -> new ApiException(ErrorMessages.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));

                    if (!product.getProvider().getId().equals(provider.getId())) {
                        throw new ApiException(ErrorMessages.PRODUCT_NOT_AVAILABLE, HttpStatus.BAD_REQUEST);
                    }

                    if (e.quantity() == null || e.quantity() <= 0) {
                        throw new ApiException(ErrorMessages.INVALID_QUANTITY, HttpStatus.BAD_REQUEST);
                    }

                    if (product.getStock() == null || product.getStock() < e.quantity()) {
                        throw new ApiException(ErrorMessages.INSUFFICIENT_STOCK, HttpStatus.BAD_REQUEST);
                    }

                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(e.quantity());
                    product.setStock(product.getStock() - e.quantity());
                    orderItem.setProduct(product);
                    orderItem.setPriceAtPurchase(product.getPrice());
                    productsToUpdate.add(product);

                    return orderItem;
                }
        ).toList();

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setProvider(provider);
        order.setOrderItems(orderItemList);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderType(orderCreationRequest.orderType());

        order.setTotalPrice(orderItemList.stream().mapToDouble(
                item -> item.getPriceAtPurchase() * item.getQuantity()
        ).sum());

        orderItemList.forEach(item -> item.setOrder(order));

        // TODO don't forget to set the delivery date when approving in the order
        orderRepository.save(order);
        productRepository.saveAll(productsToUpdate);
        orderItemRepository.saveAll(orderItemList);

        return "Order Created Successfully";

    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException(ErrorMessages.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<Order> orders;

       if (user.getRole() == Role.USER) {
            orders = orderRepository.findByUserIdAndDeletedAtIsNull(user.getId());
        } else if (user.getRole() == Role.PROVIDER) {
            Provider provider = providerRepository.findByIdAndDeletedAtIsNull(user.getId())
                    .orElseThrow(() -> new ApiException(ErrorMessages.PROVIDER_NOT_FOUND, HttpStatus.NOT_FOUND));
            orders = orderRepository.findByProviderIdAndDeletedAtIsNull(provider.getId());
        } else {
            orders = orderRepository.findByDeletedAtIsNull();
        }

        return orders.stream().map(orderMapper::toDto).toList();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'PROVIDER')")
    public String confirmOrder(String email, String orderId, ApproveOrderRequest approveOrderRequest) {
        User actor = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.UNAUTHORIZED));

        Order order =  orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ApiException("Order not found", HttpStatus.NOT_FOUND));

        ensureCanManageOrder(actor, order);

        order.setOrderStatus(approveOrderRequest.approved() ?  OrderStatus.APPROVED : OrderStatus.REJECTED);
        order.setDeliveryDate(approveOrderRequest.deliveryDate());

        if (! approveOrderRequest.approved()) {
            order.setRejectionReason(approveOrderRequest.rejectionReason());
        }

        orderRepository.save(order);

        return "Order Confirmed";
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'PROVIDER')")
    public OrderResponse changeOrderStatus(
            String email,
            String orderId,
            ChangeOrderStatusRequest changeOrderStatusRequest
    ) {
        User actor = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.UNAUTHORIZED));

        Order order =  orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ApiException("Order not found", HttpStatus.NOT_FOUND));

        ensureCanManageOrder(actor, order);

        if (order.getOrderStatus() == OrderStatus.PENDING) {
            throw new  ApiException("order hasn't been approved to be delivered", HttpStatus.BAD_REQUEST);
        }

        if (order.getOrderStatus().equals(OrderStatus.REJECTED)) {
            throw new  ApiException("order is rejected", HttpStatus.BAD_REQUEST);
        }

        order.setOrderStatus(changeOrderStatusRequest.orderStatus());
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    private void ensureCanManageOrder(User user, Order order) {
        if (user.getRole() == Role.ADMIN) {
            return;
        }
        String providerOwnerEmail = order.getProvider().getUser().getEmail();
        boolean isProviderOwner = providerOwnerEmail != null && providerOwnerEmail.equals(user.getEmail());
        if (!isProviderOwner) {
            throw new ApiException(ErrorMessages.UNAUTHORIZED_ACTION, HttpStatus.FORBIDDEN);
        }
    }

}
