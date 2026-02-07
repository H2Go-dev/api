package h2go.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import h2go.model.User;
import h2go.repository.UserRepository;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
  
  private UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> {
          log.error("User not found with email: {}", username);
          return new UsernameNotFoundException("user not found");
        });

    log.info("Found user: {} with email: {}", user.getName(), user.getEmail());
    log.debug("User enabled: {}", user.getEnabled());
    log.debug("User role: {}", user.getRole());
    
    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPasswordHash(),
        Collections.emptyList());
    
    log.info("Created UserDetails for user: {}", user.getEmail());
    return userDetails;
  }

}
