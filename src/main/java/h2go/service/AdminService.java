package h2go.service;

import h2go.exception.ApiException;
import h2go.model.Provider;
import h2go.model.enums.RegistrationStatus;
import h2go.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminService {

    private final ProviderRepository providerRepository;

    public void approveProvider(String id, boolean approve) {
        Provider provider = providerRepository.findByIdAndDeletedAtIsNull(id)
                        . orElseThrow(() -> new ApiException("Provider not found", HttpStatus.NOT_FOUND));

        if (!provider.getRegistrationStatus().equals(RegistrationStatus.PENDING)) {
            throw  new ApiException("Provider is already processed", HttpStatus.CONFLICT);
        }

        provider.setRegistrationStatus(approve ? RegistrationStatus.APPROVED : RegistrationStatus.REJECTED);

        providerRepository.save(provider);
    }
}
