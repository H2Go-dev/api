package h2go.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProviderRegistrationRequest(
        @NotNull
        UserRegistrationRequest user,
        @NotNull
        ProviderCreationDTO provider
) {
}
