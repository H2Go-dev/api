package h2go.service;

import h2go.dto.request.OrderCreationRequest;
import h2go.exception.ApiException;
import h2go.model.*;
import h2go.model.enums.OrderStatus;
import h2go.model.enums.RegistrationStatus;
import h2go.model.enums.SubscriptionStatus;
import h2go.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> createOrder(String email, OrderCreationRequest orderCreationRequest) {
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

        // to solve n+1 problem here
        List<Product> productsToUpdate = new ArrayList<>();
        List<OrderItem> orderItemList = orderCreationRequest.products().stream().map(
                (e) -> {

                    Product product = productRepository.findByIdAndDeletedAtIsNull(e.productId())
                            .orElseThrow(() -> new ApiException("Product not found", HttpStatus.NOT_FOUND));

                    if (!product.getProvider().getId().equals(provider.getId())) {
                        throw new ApiException("Product is not available in provider", HttpStatus.BAD_REQUEST);
                    }

                    if (e.quantity() <= 0) {
                        throw new ApiException("quantity cannot be Zero or less", HttpStatus.BAD_REQUEST);
                    }

                    if (product.getStock() < e.quantity()) {
                        throw new ApiException("Product quantity less than stock", HttpStatus.BAD_REQUEST);
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

        return new ResponseEntity<String>("Order Created Successfully", HttpStatus.CREATED);

    }
}
