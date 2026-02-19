package h2go.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ProviderRegistrationRequest(
        @Valid
        @NotNull
        UserRegistrationRequest user,
        @Valid
        @NotNull
        ProviderCreationDTO provider
) {
}
