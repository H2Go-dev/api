package h2go.controller;

import h2go.dto.request.ApproveSubscriptionRequest;
import h2go.dto.response.SubscriptionRetrievalResponse;
import h2go.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return subscriptionService.addSubscription(userDetails.getUsername(), providerId);
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
        return subscriptionService.approveSubscription(
                userDetails.getUsername(),
                subscriptionId,
                approveSubscriptionRequest
        );
    }

    @PutMapping("/cancel/{subscriptionId}")
    public ResponseEntity<SubscriptionRetrievalResponse> cancelSubscription(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @PathVariable
            String subscriptionId
    ) {
        return subscriptionService.cancelSubscription(userDetails.getUsername(), subscriptionId);
    }

}
