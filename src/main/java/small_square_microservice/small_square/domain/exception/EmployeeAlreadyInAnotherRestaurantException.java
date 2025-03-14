package small_square_microservice.small_square.domain.exception;

public class EmployeeAlreadyInAnotherRestaurantException extends RuntimeException {
    public EmployeeAlreadyInAnotherRestaurantException(String message) {
        super(message);
    }
}
