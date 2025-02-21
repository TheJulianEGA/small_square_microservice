package small_square_microservice.small_square.domain.exception;

public class CategoryNotFundException extends RuntimeException {
    public CategoryNotFundException(String message) {
        super(message);
    }
}
