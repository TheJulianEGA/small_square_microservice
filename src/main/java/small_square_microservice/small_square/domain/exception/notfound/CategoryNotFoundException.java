package small_square_microservice.small_square.domain.exception.notfound;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
