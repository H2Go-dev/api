package h2go.controller;

import h2go.dto.request.LoginRequest;
import h2go.dto.request.ProviderRegistrationRequest;
import h2go.dto.request.UserRegistrationRequest;
import h2go.dto.response.ProviderRetrievalResponse;
import h2go.dto.response.UserRetrievalResponse;
import h2go.service.AuthService;
import h2go.service.ProviderService;
import h2go.service.UserService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<UserRetrievalResponse> register(@Valid @RequestBody UserRegistrationRequest userDTO) {
    UserRetrievalResponse userRetrievalResponse = userService.createUser(userDTO);
      return ResponseEntity.status(HttpStatus.CREATED.value()).body(userRetrievalResponse);
  }

  @PostMapping("/register/provider")
  public ResponseEntity<ProviderRetrievalResponse> registerProvider(
          @Valid
          @RequestBody
          ProviderRegistrationRequest providerDTO
  ) {
       ProviderRetrievalResponse providerRetrievalResponse =  providerService.register(providerDTO);
       return ResponseEntity.status(HttpStatus.CREATED.value()).body(providerRetrievalResponse);
  }

  @PostMapping("/login")
  public String login(@Valid @RequestBody LoginRequest userDTO) {
      return authService.login(userDTO);
  }

}
