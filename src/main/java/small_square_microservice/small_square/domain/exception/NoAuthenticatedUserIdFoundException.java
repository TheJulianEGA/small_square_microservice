package small_square_microservice.small_square.domain.exception;

public class NoAuthenticatedUserIdFoundException extends RuntimeException {
    public NoAuthenticatedUserIdFoundException(String message) {
        super(message);
    }
}
