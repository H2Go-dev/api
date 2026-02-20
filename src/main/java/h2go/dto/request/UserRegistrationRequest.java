package h2go.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public record UserRegistrationRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @NotNull
        String name,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @NotNull
        String email,
        
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
        @Column(length = 15)
        String phoneNumber,
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @NotNull
        String password
        
) {
}
