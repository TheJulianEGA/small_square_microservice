package small_square_microservice.small_square.domain.api;

import small_square_microservice.small_square.domain.model.MessageModel;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IOrderServicePort {

    Order createOrder(Order order);

    Paginated<Order> getOrdersByStatus( String status, int page, int size);

    Order assignOrder(Long orderId);

    MessageModel orderReady(Long orderId);

    Order orderDelivery(Long orderId, Integer securityCode);

    Order cancelOrder(Long orderId);
}
