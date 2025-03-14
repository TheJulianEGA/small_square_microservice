package small_square_microservice.small_square.domain.exception.notfound;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(String message) {
        super(message);
    }
}
