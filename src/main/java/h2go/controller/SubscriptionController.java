package h2go.controller;

import h2go.dto.request.ApproveSubscriptionRequest;
import h2go.dto.response.SubscriptionRetrievalResponse;
import h2go.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionRetrievalResponse> getSubscriptions(@AuthenticationPrincipal UserDetails userDetails) {
        return subscriptionService.findAll(userDetails.getUsername());
    }

    @PostMapping("/{providerId}")
    public ResponseEntity<SubscriptionRetrievalResponse> createSubscription(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @PathVariable
            String providerId
    ) {
        SubscriptionRetrievalResponse subscription = subscriptionService.addSubscription(
                userDetails.getUsername(),
                providerId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    @PutMapping("/approve/{subscriptionId}")
    public ResponseEntity<SubscriptionRetrievalResponse> approveSubscription(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @PathVariable
            String subscriptionId,
            @Valid @RequestBody
            ApproveSubscriptionRequest approveSubscriptionRequest
    ) {
        SubscriptionRetrievalResponse subscription = subscriptionService.approveSubscription(
                userDetails.getUsername(),
                subscriptionId,
                approveSubscriptionRequest
        );
        return ResponseEntity.ok(subscription);
    }

    @PutMapping("/cancel/{subscriptionId}")
    public ResponseEntity<SubscriptionRetrievalResponse> cancelSubscription(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @PathVariable
            String subscriptionId
    ) {
        SubscriptionRetrievalResponse subscription = subscriptionService.cancelSubscription(
                userDetails.getUsername(),
                subscriptionId
        );
        return ResponseEntity.ok(subscription);
    }

}
