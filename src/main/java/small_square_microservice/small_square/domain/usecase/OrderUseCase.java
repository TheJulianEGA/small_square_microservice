package small_square_microservice.small_square.domain.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.exception.DishNotFundException;
import small_square_microservice.small_square.domain.exception.OrderInProgressException;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IOrderPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;

import java.time.LocalDateTime;


public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IAuthenticationSecurityPort authenticationSecurityPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.authenticationSecurityPort = authenticationSecurityPort;
    }

    @Override
    public Order createOrder(Order order) {
        Long clientId = authenticationSecurityPort.getAuthenticatedUserId();

        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId());
        if (restaurant == null) {
            throw new RestaurantNotFundException("Restaurante no encontrado");
        }

        if (orderPersistencePort.hasPendingOrInProgressOrder(clientId)) {
            throw new OrderInProgressException("Ya tienes un pedido en proceso");
        }

        order.setClientId(clientId);
        order.setStatus("Pendiente");
        order.setDate(LocalDateTime.now());

        return orderPersistencePort.createOrder(order);
    }
}