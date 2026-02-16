package h2go.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductCreationalRequest(
        @NotNull(message = "name can't be null")
        String name,

        @NotNull
        @DecimalMin(value = "0.1", message = "price can't be zero or negative")
        Double price,

        @NotNull
        @DecimalMin(value = "0.1", message = "volume can't be zero or negative")
        Double volume,

        @Min(value = 0, message = "stock can't be zero or less")
        @NotNull
        Integer stock,

        String nutritionalFacts
) {
}
