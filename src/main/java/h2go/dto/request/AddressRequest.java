package h2go.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
    @NotBlank(message = "Password is required")
    String addressDetails
) {
}
