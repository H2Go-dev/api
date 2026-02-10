package h2go.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(final String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    public ApiException(String message, Throwable cause) {
        this(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public ApiException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

}
