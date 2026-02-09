package h2go.service;

import h2go.dto.UserCreationDTO;
import h2go.dto.UserRetrievalDTO;
import h2go.exception.ApiException;
import h2go.mapper.UserMapper;
import h2go.model.User;
import h2go.model.enums.Role;
import h2go.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public UserService( PasswordEncoder passwordEncoder, UserMapper userMapper, UserRepository userRepository) {
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public List<UserRetrievalDTO> getAllUsers() {
    return userMapper.toDtoList(userRepository.findAll());
  }

  public void createUser(UserCreationDTO userCreationDTO) {
    if (userRepository.findByEmail(userCreationDTO.email()).isPresent()) {
      throw new ApiException("Email already exists: " + userCreationDTO.email(), HttpStatus.CONFLICT);
    }
    
    User user = userMapper.toEntity(userCreationDTO, passwordEncoder);
    userRepository.save(user);
  }

  public UserRetrievalDTO getUserById(String id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ApiException("User not found with id: " + id, HttpStatus.NOT_FOUND));
    return userMapper.toDto(user);
  }

  public void deleteUser(String id) {
    if (!userRepository.existsById(id)) {
      throw new ApiException("User not found with id: " + id, HttpStatus.NOT_FOUND);
    }
    userRepository.deleteById(id);
  }

  public UserRetrievalDTO getUserProfile(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException("User not found with email: " + email, HttpStatus.NOT_FOUND));
    // TODO: to add the order details and history when orders are implemented
    return userMapper.toDto(user);

  }
}
