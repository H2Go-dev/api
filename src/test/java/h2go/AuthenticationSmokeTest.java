package h2go;

import com.fasterxml.jackson.databind.ObjectMapper;
import h2go.dto.request.LoginRequest;
import h2go.dto.request.UserRegistrationRequest;
import h2go.dto.response.UserRetrievalResponse;
import h2go.model.Provider;
import h2go.model.enums.Role;
import h2go.repository.ProviderRepository;
import h2go.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthenticationSmokeTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        providerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testUserRegistrationAndLoginFlow() throws Exception {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
            "John Doe",
            "john.doe@example.com",
            "+1234567890",
            "SecurePassword123!"
        );

        mockMvc.perform(post("/api/auth/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.name", is("John Doe")));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("john.doe@example.com", "SecurePassword123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(emptyString())))
                .andExpect(jsonPath("$", matchesPattern("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$")));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("nonexistent@example.com", "wrongpassword"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegistrationWithDuplicateEmail() throws Exception {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
            "Jane Smith",
            "jane.smith@example.com",
            "+1234567890",
            "Password123!"
        );

        mockMvc.perform(post("/api/auth/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict());
    }
}