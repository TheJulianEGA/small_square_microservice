package small_square_microservice.small_square.domain.api;

import small_square_microservice.small_square.domain.model.Order;

public interface IOrderServicePort {

    Order createOrder(Order order);
}
