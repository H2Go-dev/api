package h2go.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductCreationalRequest(
        @NotNull
        String name,

        @NotNull
        @Min(value = 0, message = "price can't be zero or negative")
        Double price,

        @NotNull
        @Min(value = 0, message = "volume can't be zero or negative")
        Double volume,

        @Min(value = 0, message = "stock can't be zero or less")
        @NotNull
        Integer stock,

        String nutritionalFacts
) {
}
