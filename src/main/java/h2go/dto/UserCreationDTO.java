package h2go.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreationDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @Column(nullable = false)
        String name,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Column(nullable = false)
        String email,
        
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
        @Column(length = 15)
        String phoneNumber,
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @Column(nullable = false)
        String password
        
) {
}
