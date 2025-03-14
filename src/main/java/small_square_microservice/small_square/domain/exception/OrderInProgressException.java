package small_square_microservice.small_square.domain.exception;

public class OrderInProgressException extends RuntimeException {
  public OrderInProgressException(String message) {
    super(message);
  }
}
