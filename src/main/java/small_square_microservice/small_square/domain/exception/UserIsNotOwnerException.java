package small_square_microservice.small_square.domain.exception;

public class UserIsNotOwnerException extends RuntimeException {
    public UserIsNotOwnerException(String message) {
        super(message);
    }
}
