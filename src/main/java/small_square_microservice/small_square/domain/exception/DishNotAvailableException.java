package small_square_microservice.small_square.domain.exception;

public class DishNotAvailableException extends RuntimeException {
    public DishNotAvailableException(String message) {
        super(message);
    }
}
