package h2go.controller;

import h2go.dto.LoginDTO;
import h2go.dto.ProviderRegistrationDTO;
import h2go.dto.UserCreationDTO;
import h2go.service.AuthService;
import h2go.service.ProviderService;
import h2go.service.UserService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private static final Logger log = LoggerFactory.getLogger(AuthController.class);
  
  private final UserService userService;

  private final AuthService authService;

  private final ProviderService providerService;


  @PostMapping("/register/user")
  public void register(@Valid @RequestBody UserCreationDTO userDTO) {
    userService.createUser(userDTO);
  }

  @PostMapping("/register/provider")
  public void registerProvider(@Valid @RequestBody ProviderRegistrationDTO providerDTO) {
      providerService.register(providerDTO);
  }

  @PostMapping("/login")
  public String login(@Valid @RequestBody LoginDTO userDTO) {
      return authService.login(userDTO);
  }

}
