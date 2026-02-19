package h2go.service;

import h2go.dto.request.ApproveSubscriptionRequest;
import h2go.dto.response.SubscriptionRetrievalResponse;
import h2go.exception.ApiException;
import h2go.mapper.SubscriptionMapper;
import h2go.model.Provider;
import h2go.model.Subscription;
import h2go.model.User;
import h2go.model.enums.RegistrationStatus;
import h2go.model.enums.Role;
import h2go.model.enums.SubscriptionStatus;
import h2go.repository.ProviderRepository;
import h2go.repository.SubscriptionRepository;
import h2go.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    private final SubscriptionMapper subscriptionMapper;

    private final ProviderRepository providerRepository;

    @Transactional(readOnly = true)
    public List<SubscriptionRetrievalResponse> findAll(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("user not found", HttpStatus.UNAUTHORIZED));

        if (user.getRole().equals(Role.PROVIDER)) {
            Provider provider = providerRepository.findByIdAndDeletedAtIsNull(user.getId())
                    .orElseThrow(() -> new ApiException("provider not found", HttpStatus.UNAUTHORIZED));
            return provider.getSubscriptions().stream().map(subscriptionMapper::toDto).toList();
        }

        return  user.getSubscriptions().stream().map(subscriptionMapper::toDto).toList();

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Transactional
    public SubscriptionRetrievalResponse addSubscription(String email, String providerId) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("user not found", HttpStatus.UNAUTHORIZED));

        Provider provider = providerRepository.findByIdAndDeletedAtIsNull(providerId)
                .orElseThrow(() -> new ApiException("provider not found", HttpStatus.BAD_REQUEST));

        if (provider.getRegistrationStatus() != RegistrationStatus.APPROVED) {
            throw new  ApiException("provider not approved", HttpStatus.BAD_REQUEST);
        }

        Subscription subscription = new Subscription();
        subscription.setProvider(provider);
        subscription.setUser(user);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(savedSubscription);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROVIDER')")
    public SubscriptionRetrievalResponse approveSubscription(
            String email,
            String subscriptionId,
            ApproveSubscriptionRequest approveSubscriptionRequest
    ) {
        Subscription subscription = subscriptionRepository.findByIdAndDeletedAtIsNull(subscriptionId)
                .orElseThrow(() -> new ApiException("subscription not found", HttpStatus.BAD_REQUEST));

        if (subscription.getSubscriptionStatus() != SubscriptionStatus.PENDING) {
            throw new  ApiException("subscription already processed", HttpStatus.BAD_REQUEST);
        }

        if (!subscription.getProvider().getUser().getEmail().equals(email)) {
            throw new   ApiException("can't approve this subscription", HttpStatus.UNAUTHORIZED);
        }

        subscription.setSubscriptionStatus(
                approveSubscriptionRequest.approved() ? SubscriptionStatus.APPROVED : SubscriptionStatus.REJECTED
        );

        if (!approveSubscriptionRequest.approved()) {
            subscription.setRejectionReason(approveSubscriptionRequest.rejectionReason());
        }

        subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(subscription);
    }


    @Transactional
    public SubscriptionRetrievalResponse cancelSubscription(String email, String subscriptionId) {
        Subscription subscription = subscriptionRepository.findByIdAndDeletedAtIsNull(subscriptionId)
                .orElseThrow(() -> new ApiException("subscription not found", HttpStatus.BAD_REQUEST));

        if (
            !subscription.getUser().getEmail().equals(email)
                && !subscription.getProvider().getUser().getEmail().equals(email)
        ) {
            throw new ApiException("can't cancel this subscription", HttpStatus.UNAUTHORIZED);
        }

        subscription.endSubscription();

        subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }
}
