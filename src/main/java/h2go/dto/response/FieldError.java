package h2go.dto.response;

public record FieldError(
    String field,
    String message,
    Object rejectedValue
) {
    public FieldError {
        if (field == null || field.trim().isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be null or empty");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
    }
}