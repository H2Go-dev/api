package h2go.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserCreationDTO(
        @Column(nullable = false)
        String name,
        @Email(message="Invalid Email format")
        @Column(nullable = false)
        String email,
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
        @Column(length = 15)
        String phoneNumber,
        @Column(nullable = false)
        String password,
        String role
) {
}
