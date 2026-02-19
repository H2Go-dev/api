package h2go.controller;

import h2go.dto.request.ApproveOrderRequest;
import h2go.dto.request.ChangeOrderStatusRequest;
import h2go.dto.request.OrderCreationRequest;
import h2go.dto.response.OrderResponse;
import h2go.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getMyOrders(userDetails.getUsername());
    }

    @PostMapping
    public ResponseEntity<String> addOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCreationRequest orderCreationRequest
    ) {
        String responseMessage = orderService.createOrder(userDetails.getUsername(), orderCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @PutMapping("/approve/{orderId}")
    public ResponseEntity<String> approveOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @Valid @RequestBody ApproveOrderRequest approveOrderRequest
    ) {
        String responseMessage = orderService.confirmOrder(userDetails.getUsername(), orderId, approveOrderRequest);
        return ResponseEntity.ok(responseMessage);
    }

    @PutMapping("/status/{orderId}")
    public OrderResponse changeOrderStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @Valid @RequestBody ChangeOrderStatusRequest changeOrderStatusRequest
    ) {
        return orderService.changeOrderStatus(userDetails.getUsername(), orderId, changeOrderStatusRequest);
    }
}
