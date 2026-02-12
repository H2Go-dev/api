package h2go.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

public record ProviderCreationDTO(
        @NotBlank(message = "Business Name is required")
        @Column(nullable = true)
        String businessName,

        @NotBlank(message = "Service City is required")
        @Column(nullable = true)
        String serviceCity
) {

}
