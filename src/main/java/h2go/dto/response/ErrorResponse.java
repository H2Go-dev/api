package h2go.dto.response;

import h2go.dto.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    int status,
    String message,
    List<FieldError> errors,
    LocalDateTime timestamp
) {
    public ErrorResponse {
        if (errors == null || errors.isEmpty()) {
            throw new IllegalArgumentException("Errors list cannot be null or empty");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
    }
}