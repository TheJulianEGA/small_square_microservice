package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.exception.*;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IOrderPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;

import java.time.LocalDateTime;
import java.util.List;


public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;
    private final IDishPersistencePort dishPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IAuthenticationSecurityPort authenticationSecurityPort,
                        IDishPersistencePort dishPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.authenticationSecurityPort = authenticationSecurityPort;
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public Order createOrder(Order order) {
        Long clientId = authenticationSecurityPort.getAuthenticatedUserId();

        Restaurant restaurant = validateRestaurant(order.getRestaurant().getId());
        validateNoPendingOrder(clientId);
        validateOrderDishes(order.getOrderDishes(), restaurant);

        order.setClientId(clientId);
        order.setStatus(DomainConstants.STATUS_PENDING);
        order.setDate(LocalDateTime.now());

        return orderPersistencePort.createOrder(order);
    }

    @Override
    public Paginated<Order> getOrdersByStatus( String status, int page, int size) {

        Long authenticatedEmployeeId = authenticationSecurityPort.getAuthenticatedUserId();

        Long restaurantId = restaurantPersistencePort.getRestaurantByEmployeeId(authenticatedEmployeeId);

        if (restaurantId == null) {
            throw new UnauthorizedException(DomainConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT);
        }

        if (page < 0 || size < 1) {
            throw new InvalidPaginationException(DomainConstants.INVALID_PAGINATION_MESSAGE);
        }

        return orderPersistencePort.getOrdersByStatus(restaurantId, status, page, size);
    }


    private Restaurant validateRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new RestaurantNotFoundException(DomainConstants.RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }

    private void validateNoPendingOrder(Long clientId) {
        if (orderPersistencePort.hasPendingOrInProgressOrder(clientId)) {
            throw new OrderInProgressException(DomainConstants.ORDER_IN_PROGRESS);
        }
    }

    private void validateOrderDishes(List<OrderDish> orderDishes, Restaurant restaurant) {
        for (OrderDish orderDish : orderDishes) {
            Dish dish = dishPersistencePort.getDishById(orderDish.getDish().getId());

            if (dish == null || !dish.getRestaurant().getId().equals(restaurant.getId())) {
                throw new DishNotFoundException(DomainConstants.DISH_NOT_FOUND);
            }

            if (Boolean.FALSE.equals(dish.getIsActive())) {
                throw new DishNotAvailableException(DomainConstants.DISH_NOT_AVAILABLE);
            }
        }
    }
}