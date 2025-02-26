package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IOrderPersistencePort {

    boolean hasPendingOrInProgressOrder(Long clientId,Long restaurantId);

    Order createOrder(Order order);

    Paginated<Order> getOrdersByStatus(Long restaurantId, String status, int page, int size);

    Order getOrderById(Long orderId);

    boolean isOrderAssignedToEmployee(Long orderId, Long employeeId);

    Order updateOrder(Order order);
}
