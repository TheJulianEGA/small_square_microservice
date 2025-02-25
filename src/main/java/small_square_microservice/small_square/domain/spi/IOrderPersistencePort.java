package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IOrderPersistencePort {
    boolean hasPendingOrInProgressOrder(Long clientId);

    Order createOrder(Order order);

    Paginated<Order> getOrdersByStatus(Long restaurantId, String status, int page, int size);
}
