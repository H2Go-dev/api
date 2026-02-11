package h2go.service;

import h2go.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import h2go.config.JwtUtil;
import h2go.dto.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
  private static final Logger log = LoggerFactory.getLogger(AuthService.class);
  
  private final AuthenticationManager authenticationManager;

  private final JwtUtil jwtUtil;

  public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  public String login(LoginDTO userDTO) {

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDTO.email(), userDTO.password());

      Authentication authentication = authenticationManager.authenticate(authToken);
      final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      if (userDetails == null) {
        log.error("Authentication failed for user: {} with error: User details is null", userDTO.email());
        throw new ApiException("User doesn't exist", HttpStatus.NOT_FOUND);
      }
      log.info("Login in Successful for:{}", userDetails.getUsername());
      return jwtUtil.generateToken(userDetails.getUsername());
  }

}
