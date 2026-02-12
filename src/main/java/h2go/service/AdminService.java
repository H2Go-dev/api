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
                        . orElseThrow(() -> new ApiException("provider not found", HttpStatus.NOT_FOUND));


        if (approve) {
            provider.setRegistrationStatus(RegistrationStatus.APPROVED);
        } else  {
            provider.setRegistrationStatus(RegistrationStatus.REJECTED);
        }

        providerRepository.save(provider);
    }
}
