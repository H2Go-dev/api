package h2go.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProviderCreationDTO(
        @NotBlank(message = "Business Name is required")
        String businessName,

        @NotBlank(message = "Service City is required")
        String serviceCity
) {

}
