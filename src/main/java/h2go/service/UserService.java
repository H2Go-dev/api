package h2go.service;

import h2go.dto.request.UserRegistrationRequest;
import h2go.dto.response.UserRetrievalResponse;
import h2go.exception.ApiException;
import h2go.mapper.UserMapper;
import h2go.model.Provider;
import h2go.model.User;
import h2go.repository.ProviderRepository;
import h2go.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ProviderRepository providerRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<UserRetrievalResponse> getAllUsers(Integer page, Integer size) {
        if (page == null || size == null || size <= 0 || page < 0 || size > 100) {
            throw new ApiException("invalid page or size parameter", HttpStatus.BAD_REQUEST);
        }
        Page<User> users = userRepository.findAllByDeletedAtIsNullOrderByIdAsc(PageRequest.of(page, size));
        return users.map(userMapper::toDto);
    }

    @Transactional
    public UserRetrievalResponse createUser(UserRegistrationRequest userRegistrationRequest) {
        if (userRepository.findByEmail(userRegistrationRequest.email()).isPresent()) {
            throw new ApiException("A user with this email already exists", HttpStatus.CONFLICT);
        }
    
        User user = userMapper.toEntity(userRegistrationRequest, passwordEncoder);
        return userMapper.toDto(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public UserRetrievalResponse getUserById(String id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        return userMapper.toDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow( () -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        user.softDelete();

        Optional<Provider> opProvider = providerRepository.findById(user.getId());
        if (opProvider.isPresent()) {
            Provider provider = opProvider.get();
            provider.softDelete();
            providerRepository.save(provider);
        }

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserRetrievalResponse getUserProfile(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow( () -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        // TODO: to add the order details and history when orders are implemented
        return userMapper.toDto(user);
    }

    @Transactional
    public UserRetrievalResponse updateUser(String email, UserRegistrationRequest userRegistrationRequest) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        if (!user.getEmail().equals(userRegistrationRequest.email())
                && userRepository.findByEmail(userRegistrationRequest.email()).isPresent()) {
                throw new ApiException("A user with this email already exists", HttpStatus.CONFLICT);
        }

        userMapper.updateEntityFromDto(userRegistrationRequest, user, passwordEncoder);
        return userMapper.toDto(userRepository.save(user));
    }
}
