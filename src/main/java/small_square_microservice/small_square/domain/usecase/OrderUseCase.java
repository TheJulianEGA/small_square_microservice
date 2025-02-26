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
import java.util.Objects;


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
        validateNoPendingOrder(clientId,restaurant.getId());
        validateOrderDishes(order.getOrderDishes(), restaurant);

        order.setClientId(clientId);
        order.setStatus(DomainConstants.STATUS_PENDING);
        order.setOrderPendingDate(LocalDateTime.now());

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

    @Override
    public Order assignOrder(Long orderId) {
        Long employeeId = authenticationSecurityPort.getAuthenticatedUserId();
        Order order = orderPersistencePort.getOrderById(orderId);

        validateOrderBelongsToEmployeeRestaurant(order, employeeId);
        validateOrderNotAssignedToAnotherEmployee(order, employeeId);
        validateOrderNotAlreadyAssignedToEmployee(orderId, employeeId);

        order.setChefId(employeeId);
        order.setOrderPreparationDate(LocalDateTime.now());

        return orderPersistencePort.updateOrder(order);
    }

    private void validateOrderBelongsToEmployeeRestaurant(Order order, Long employeeId) {
        Long employeeRestaurantId = restaurantPersistencePort.getRestaurantByEmployeeId(employeeId);
        if (!Objects.equals(order.getRestaurant().getId(), employeeRestaurantId)) {
            throw new OrderNotFoundException(DomainConstants.ORDER_NOT_FOUND);
        }
    }

    private void validateOrderNotAssignedToAnotherEmployee(Order order, Long employeeId) {
        if (order.getChefId() != null && !Objects.equals(order.getChefId(), employeeId)) {
            throw new OrderAlreadyAssignedException(DomainConstants.ORDER_ASSIGNED_TO_ANOTHER_EMPLOYEE);
        }
    }

    private void validateOrderNotAlreadyAssignedToEmployee(Long orderId, Long employeeId) {
        if (orderPersistencePort.isOrderAssignedToEmployee(orderId, employeeId)) {
            throw new OrderAlreadyAssignedException(DomainConstants.ORDER_ASSIGNED_TO_EMPLOYEE);
        }
    }

    private Restaurant validateRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new RestaurantNotFoundException(DomainConstants.RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }

    private void validateNoPendingOrder(Long clientId, Long restaurantId) {
        if (orderPersistencePort.hasPendingOrInProgressOrder(clientId, restaurantId)) {
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