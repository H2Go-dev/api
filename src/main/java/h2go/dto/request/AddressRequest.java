package h2go.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
    @NotBlank(message = "Address details is required")
    String addressDetails
) {
}
