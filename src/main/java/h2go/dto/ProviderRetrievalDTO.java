package h2go.dto;

import h2go.model.enums.RegistrationStatus;

import java.time.LocalDateTime;

public record ProviderRetrievalDTO(
        String id,
        UserRetrievalDTO user,
        String businessName,
        String serviceCity,
        RegistrationStatus registrationStatus,

        LocalDateTime deletedAt

) {
}
