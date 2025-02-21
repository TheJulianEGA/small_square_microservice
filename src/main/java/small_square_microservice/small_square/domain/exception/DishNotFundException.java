package small_square_microservice.small_square.domain.exception;

public class DishNotFundException extends RuntimeException {
    public DishNotFundException(String message) {
        super(message);
    }
}
