package h2go.service;

import h2go.dto.request.UserRegistrationRequest;
import h2go.dto.request.UserRetrievalRequest;
import h2go.exception.ApiException;
import h2go.mapper.UserMapper;
import h2go.model.Provider;
import h2go.model.User;
import h2go.repository.ProviderRepository;
import h2go.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ProviderRepository providerRepository;


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserRetrievalRequest> getAllUsers(Boolean deleted) {
        if (!deleted) {
            return userMapper.toDtoList(userRepository.findByDeletedAtIsNull());
        }
        return userMapper.toDtoList(userRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createUser(UserRegistrationRequest userRegistrationRequest) {
        if (userRepository.findByEmail(userRegistrationRequest.email()).isPresent()) {
            throw new ApiException("Email already exists: " + userRegistrationRequest.email(), HttpStatus.CONFLICT);
        }
    
        User user = userMapper.toEntity(userRegistrationRequest, passwordEncoder);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserRetrievalRequest getUserById(String id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new ApiException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        if (user.isDeleted()) {
            throw new ApiException("User has been deleted", HttpStatus.CONFLICT);
        }

        return userMapper.toDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ApiException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        if (user.isDeleted()) {
            throw new ApiException("User has been deleted", HttpStatus.CONFLICT);
        }

        user.softDelete();

        Optional<Provider> opProvider = providerRepository.findById(user.getId());
        if (opProvider.isPresent()) {
            Provider provider = opProvider.get();
            provider.softDelete();
            providerRepository.save(provider);
        }

        userRepository.save(user);
    }

  public UserRetrievalRequest getUserProfile(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(
            () -> new ApiException("User not found with email: " + email, HttpStatus.NOT_FOUND));
    if (user.isDeleted()) {
        throw new ApiException("User has been deleted", HttpStatus.CONFLICT);
    }
    // TODO: to add the order details and history when orders are implemented
    return userMapper.toDto(user);
  }
}
