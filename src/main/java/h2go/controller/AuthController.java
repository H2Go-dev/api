package h2go.controller;

import h2go.config.JwtUtil;
import h2go.dto.LoginDTO;
import h2go.dto.UserCreationDTO;
import h2go.service.AuthService;
import h2go.service.UserService;
import jakarta.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private static final Logger log = LoggerFactory.getLogger(AuthController.class);
  
  private final UserService userService;
  private final AuthService authService;

  public AuthController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
    log.info("AuthController initialized");
  }

  @PostMapping("/register")
  public void register(@Valid @RequestBody UserCreationDTO userDTO) {
    userService.createUser(userDTO);
  }

  @PostMapping("/login")
  public String login(@Valid @RequestBody LoginDTO userDTO) {
    try {
      String token = authService.login(userDTO);
      log.info("Login successful for user: {}", userDTO.email());
      return token;
    } catch (Exception e) {
      log.error("Login failed for user: {} with error: {}", userDTO.email(), e.getMessage());
      log.error("Login exception details:", e);
      throw e;
    }
  }

}
