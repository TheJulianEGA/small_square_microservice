package small_square_microservice.small_square.domain.exception.notfound;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
