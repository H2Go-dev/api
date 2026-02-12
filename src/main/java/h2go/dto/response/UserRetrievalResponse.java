package h2go.dto.response;

import h2go.model.enums.Role;

import java.time.LocalDateTime;

public record UserRetrievalResponse(
        String id,
        String name,
        String email,
        String phoneNumber,
        Role role,
        Boolean enabled,
        LocalDateTime deletedAt
) {
}
