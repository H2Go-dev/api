package h2go.service;

import h2go.dto.request.ProviderRegistrationRequest;
import h2go.mapper.ProviderMapper;
import h2go.mapper.UserMapper;
import h2go.model.Provider;
import h2go.model.User;
import h2go.model.enums.RegistrationStatus;
import h2go.model.enums.Role;
import h2go.repository.ProviderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderService {
    private final ProviderRepository providerRepository;

    private final ProviderMapper providerMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public void register(@Valid ProviderRegistrationRequest providerDTO) {
        Provider provider = providerMapper.toEntity(providerDTO.provider());
        User user = userMapper.toEntity(providerDTO.user(), passwordEncoder);

        user.setRole(Role.PROVIDER);
        provider.setUser(user);
        provider.setRegistrationStatus(RegistrationStatus.PENDING);

        providerRepository.save(provider);
    }
}
