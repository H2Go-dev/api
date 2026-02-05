package h2go.service;

import h2go.dto.UserCreationDTO;
import h2go.dto.UserRetrievalDTO;
import h2go.mapper.UserMapper;
import h2go.model.User;
import h2go.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<UserRetrievalDTO> getAllUsers(){
        return userMapper.toDtoList(userRepository.findAll());
    }

    public void createUser(UserCreationDTO userCreationDTO) {
        User user = userMapper.toEntity(userCreationDTO, passwordEncoder);
        userRepository.save(user);
    }
}
