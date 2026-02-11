package h2go.dto;

import jakarta.validation.constraints.NotNull;

public record ProviderRegistrationDTO(
        @NotNull
        UserCreationDTO user,
        @NotNull
        ProviderCreationDTO provider
) {
}
