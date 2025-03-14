package small_square_microservice.small_square.domain.exception;

public class OrderAlreadyAssignedException extends RuntimeException {
    public OrderAlreadyAssignedException(String message) {
        super(message);
    }
}
