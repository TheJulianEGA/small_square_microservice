package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Order;

public interface IOrderPersistencePort {
    boolean hasPendingOrInProgressOrder(Long clientId);

    Order createOrder(Order order);
}
