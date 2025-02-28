package small_square_microservice.small_square.domain.exception;

public class InvalidSecurityCodeException extends RuntimeException {
    public InvalidSecurityCodeException(String message) {
        super(message);
    }
}
