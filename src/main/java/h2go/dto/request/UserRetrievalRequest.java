package h2go.dto.request;

import h2go.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record UserRetrievalRequest(
        String id,
        String name,
        @Email(message = "Invalid email format")
        String email,
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
        String phoneNumber,
        Role role,
        Boolean enabled,
        LocalDateTime deletedAt
) {
}
