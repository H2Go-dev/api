package h2go.service;

import h2go.dto.request.ProviderRegistrationRequest;
import h2go.dto.response.ProviderRetrievalResponse;
import h2go.exception.ApiException;
import h2go.mapper.ProviderMapper;
import h2go.mapper.UserMapper;
import h2go.model.Provider;
import h2go.model.User;
import h2go.model.enums.RegistrationStatus;
import h2go.model.enums.Role;
import h2go.repository.ProviderRepository;
import h2go.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProviderService {
    private final ProviderRepository providerRepository;

    private final UserRepository  userRepository;

    private final ProviderMapper providerMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Transactional
    public ProviderRetrievalResponse register(@Valid ProviderRegistrationRequest providerDTO) {
        Provider provider = providerMapper.toEntity(providerDTO.provider());
        User user = userMapper.toEntity(providerDTO.user(), passwordEncoder);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ApiException("email already exists", HttpStatus.CONFLICT);
        }

        user.setRole(Role.PROVIDER);
        provider.setUser(user);
        provider.setRegistrationStatus(RegistrationStatus.PENDING);

        userRepository.save(user);
        return providerMapper.toDto(providerRepository.save(provider));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Page<ProviderRetrievalResponse> getProviders(Integer page, Integer size) {
        if (page == null || size == null || size <= 0 || page < 0 || size > 100) {
            throw new ApiException("invalid page or size parameter", HttpStatus.BAD_REQUEST);
        }
        return providerRepository.findAllByDeletedAtIsNullOrderByIdAsc(PageRequest.of(page, size))
                .map(providerMapper::toDto);
    }
}
