package small_square_microservice.small_square.domain.exception;

public class RestaurantNotFundException extends RuntimeException {
    public RestaurantNotFundException(String message) {
        super(message);
    }
}
