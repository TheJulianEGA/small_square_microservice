package small_square_microservice.small_square.domain.exception;

public class EmployeeAlreadyInRestaurantException extends RuntimeException {
    public EmployeeAlreadyInRestaurantException(String message) {
        super(message);
    }
}
