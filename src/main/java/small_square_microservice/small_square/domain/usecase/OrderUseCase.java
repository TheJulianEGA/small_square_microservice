package small_square_microservice.small_square.domain.usecase;

import lombok.RequiredArgsConstructor;
import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.exception.*;
import small_square_microservice.small_square.domain.exception.notfound.DishNotFoundException;
import small_square_microservice.small_square.domain.exception.notfound.OrderNotFoundException;
import small_square_microservice.small_square.domain.exception.notfound.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.model.*;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.*;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;
    private final IDishPersistencePort dishPersistencePort;
    private final IMessageFeignPersistencePort messageFeignPersistencePort;
    private final IOrderStatusHistoryFeignPersistencePort orderStatusHistoryFeignPersistencePort;

    private static final Random RANDOM = new Random();

    @Override
    public Order createOrder(Order order) {
        Long clientId = authenticationSecurityPort.getAuthenticatedUserId();

        Restaurant restaurant = validateRestaurant(order.getRestaurant().getId());
        validateNoPendingOrder(clientId,restaurant.getId());
        validateOrderDishes(order.getOrderDishes(), restaurant);

        order.setClientId(clientId);
        order.setDate(LocalDateTime.now());

        int securityCode = generateSecurityCode();
        order.setSecurityCode(securityCode);

        String email = authenticationSecurityPort.getAuthenticatedUserEmail();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setCustomerMail(email);
        orderStatusHistory.setPreviousState(order.getStatus());

        order.setStatus(DomainConstants.STATUS_PENDING);

        Order save = orderPersistencePort.createOrder(order);
        orderStatusHistoryFeignPersistencePort.saveOrderStatusHistory(save,orderStatusHistory );

        return save;
    }

    @Override
    public Paginated<Order> getOrdersByStatus( String status, int page, int size) {

        Long authenticatedEmployeeId = authenticationSecurityPort.getAuthenticatedUserId();

        Long restaurantId = restaurantPersistencePort.getRestaurantByEmployeeId(authenticatedEmployeeId);

        if (restaurantId == null) {
            throw new UnauthorizedException(DomainConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT);
        }

        if (page < DomainConstants.MIN_NUMBER_PAGE || size < DomainConstants.MIN_NUMBER_SIZE) {
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
        order.setDate(LocalDateTime.now());

        String email = authenticationSecurityPort.getAuthenticatedUserEmail();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setEmployeeMail(email);
        orderStatusHistory.setPreviousState(order.getStatus());

        order.setStatus(DomainConstants.STATUS_IN_PREPARATION);

        Order update = orderPersistencePort.updateOrder(order);
        orderStatusHistoryFeignPersistencePort.saveOrderStatusHistory(update,orderStatusHistory );

        return update;
    }

    @Override
    public MessageModel orderReady(Long orderId) {
        Long employeeId = authenticationSecurityPort.getAuthenticatedUserId();
        Order order = orderPersistencePort.getOrderById(orderId);

        validateOrderBelongsToEmployeeRestaurant(order, employeeId);
        validateOrderStatusInPreparation(order);

        order.setDate(LocalDateTime.now());

        String email = authenticationSecurityPort.getAuthenticatedUserEmail();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setEmployeeMail(email);
        orderStatusHistory.setPreviousState(order.getStatus());

        order.setStatus(DomainConstants.STATUS_READY);

        Order update = orderPersistencePort.updateOrder(order);
        orderStatusHistoryFeignPersistencePort.saveOrderStatusHistory(update,orderStatusHistory );

        String message = generateOrderReadyMessage(orderId, order.getSecurityCode());

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(message);

        return messageFeignPersistencePort.sendWhatsAppMessage(messageModel);
    }

    @Override
    public Order orderDelivery(Long orderId, Integer securityCode) {

        Order order = orderPersistencePort.getOrderById(orderId);

        validateOrderStatusReady(order);

        validateSecurityCode(order,securityCode);

        order.setDate(LocalDateTime.now());

        String email = authenticationSecurityPort.getAuthenticatedUserEmail();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setEmployeeMail(email);
        orderStatusHistory.setPreviousState(order.getStatus());

        order.setStatus(DomainConstants.STATUS_DELIVERY);

        Order update = orderPersistencePort.updateOrder(order);
        orderStatusHistoryFeignPersistencePort.saveOrderStatusHistory(update,orderStatusHistory);

        return update;
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order order = orderPersistencePort.getOrderById(orderId);

        Long clientId = authenticationSecurityPort.getAuthenticatedUserId();
        Restaurant restaurant = validateRestaurant(order.getRestaurant().getId());

        validateOrderInProgress(clientId,restaurant.getId());

        String email = authenticationSecurityPort.getAuthenticatedUserEmail();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setEmployeeMail(email);
        orderStatusHistory.setPreviousState(order.getStatus());

        order.setStatus(DomainConstants.STATUS_CANCELED);

        Order update = orderPersistencePort.updateOrder(order);
        orderStatusHistoryFeignPersistencePort.saveOrderStatusHistory(update,orderStatusHistory);
        String message = generateOrderMessageCannotBeCancelled(orderId);

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(message);

        return update;
    }

    private void validateOrderInProgress(Long clientId, Long restaurantId) {
        if (!orderPersistencePort.isAnOrderInProcessPending(clientId, restaurantId)) {
            throw new OrderInProgressException(DomainConstants.ORDER_IN_PROGRESS);
        }
    }

    private void validateSecurityCode(Order order, Integer securityCode) {
        if (!order.getSecurityCode().equals(securityCode)) {
            throw new InvalidSecurityCodeException(DomainConstants.INVALID_SECURITY_CODE);
        }
    }

    private int generateSecurityCode() {
        return RANDOM.nextInt(10000000);
    }

    private String generateOrderReadyMessage(Long orderId, int securityCode) {
        String claimCode = String.format("%07d", securityCode);
        return String.format(DomainConstants.ORDER_READY_MESSAGE_TEMPLATE, orderId, claimCode);
    }

    private String generateOrderMessageCannotBeCancelled(Long orderId) {
        return String.format(DomainConstants.ORDER_CANNOT_BE_MESSAGE_CANCELLED_TEMPLATE, orderId);
    }

    private void validateOrderStatusInPreparation(Order order) {
        if (!DomainConstants.STATUS_IN_PREPARATION.equals(order.getStatus())) {
            throw new InvalidStatusException(DomainConstants.STATUS_IS_NOT_PREPARED);
        }

    }

    private void validateOrderStatusReady(Order order) {
        if (!DomainConstants.STATUS_READY.equals(order.getStatus())) {
            throw new InvalidStatusException(DomainConstants.STATUS_IS_NOT_READY);
        }
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