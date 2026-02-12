package h2go.service;

import h2go.dto.request.ProviderRegistrationRequest;
import h2go.dto.response.ProviderRetrievalResponse;
import h2go.mapper.ProviderMapper;
import h2go.mapper.UserMapper;
import h2go.model.Provider;
import h2go.model.User;
import h2go.model.enums.RegistrationStatus;
import h2go.model.enums.Role;
import h2go.repository.ProviderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {
    private final ProviderRepository providerRepository;

    private final ProviderMapper providerMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public ProviderRetrievalResponse register(@Valid ProviderRegistrationRequest providerDTO) {
        Provider provider = providerMapper.toEntity(providerDTO.provider());
        User user = userMapper.toEntity(providerDTO.user(), passwordEncoder);

        user.setRole(Role.PROVIDER);
        provider.setUser(user);
        provider.setRegistrationStatus(RegistrationStatus.PENDING);

        return providerMapper.toDto(providerRepository.save(provider));

    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProviderRetrievalResponse> getProviders() {
        List<ProviderRetrievalResponse> providers = providerMapper.toDtoList(providerRepository.findAll());
        // TODO: add filters and other stuff
        return providers;
    }
}
