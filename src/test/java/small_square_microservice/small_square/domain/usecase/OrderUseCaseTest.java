package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.exception.*;
import small_square_microservice.small_square.domain.exception.notfound.DishNotFoundException;
import small_square_microservice.small_square.domain.exception.notfound.OrderNotFoundException;
import small_square_microservice.small_square.domain.exception.notfound.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.model.*;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.*;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @InjectMocks
    private OrderUseCase orderUseCase;

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IAuthenticationSecurityPort authenticationSecurityPort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IMessageFeignPersistencePort messageFeignPersistencePort;

    @Mock
    private IOrderStatusHistoryFeignPersistencePort orderStatusHistoryFeignPersistencePort;


    private Order order;
    private Restaurant restaurant;
    private OrderDish orderDish;
    private Dish dish;

    @BeforeEach
    void setUp() {

        order = new Order();
        restaurant = new Restaurant();
        orderDish = new OrderDish();
        dish = new Dish();

        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        dish.setId(1L);
        dish.setName("Dish 1");
        dish.setRestaurant(restaurant);
        dish.setIsActive(true);

        orderDish.setDish(dish);
        orderDish.setQuantity(2);

        order.setId(1L);
        order.setStatus(DomainConstants.STATUS_IN_PREPARATION);
        order.setSecurityCode(1234567);
        order.setRestaurant(restaurant);
        order.setOrderDishes(List.of(orderDish));

    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully_WhenValidDataIsProvided() {
        Long clientId = 100L;

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId,restaurant.getId())).thenReturn(false);
        when(dishPersistencePort.getDishById(orderDish.getDish().getId())).thenReturn(dish);
        when(orderPersistencePort.createOrder(any(Order.class))).thenReturn(order);

        Order result = orderUseCase.createOrder(order);

        assertNotNull(result);
        assertEquals(clientId, result.getClientId());
        assertEquals(DomainConstants.STATUS_PENDING, result.getStatus());
        verify(orderPersistencePort, times(1)).createOrder(order);
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .getRestaurantById(order.getRestaurant().getId());
        verify(dishPersistencePort, times(1)).getDishById(orderDish.getDish().getId());
        verify(orderPersistencePort, times(1)).hasPendingOrInProgressOrder(clientId,restaurant.getId());
    }

    @Test
    void createOrder_ShouldThrowException_WhenRestaurantNotFound() {
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void createOrder_ShouldThrowException_WhenClientHasPendingOrder() {
        Long clientId = 100L;

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId,restaurant.getId())).thenReturn(true);

        assertThrows(OrderInProgressException.class, () -> orderUseCase.createOrder(order));

        verify(authenticationSecurityPort,times(1))
                .getAuthenticatedUserId();
        verify(restaurantPersistencePort,times(1))
                .getRestaurantById(order.getRestaurant().getId());
        verify(orderPersistencePort,times(1))
                .hasPendingOrInProgressOrder(clientId, restaurant.getId());
    }

    @Test
    void createOrder_ShouldThrowException_WhenDishNotFound() {
        Long clientId = 100L;

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId,restaurant.getId())).thenReturn(false);
        when(dishPersistencePort.getDishById(orderDish.getDish().getId())).thenReturn(null);

        assertThrows(DishNotFoundException.class, () -> orderUseCase.createOrder(order));
        verify(dishPersistencePort, times(1)).getDishById(orderDish.getDish().getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .getRestaurantById(order.getRestaurant().getId());
        verify(orderPersistencePort, times(1))
                .hasPendingOrInProgressOrder(clientId,restaurant.getId());
    }

    @Test
    void createOrder_ShouldThrowException_WhenDishIsInactive() {
        Long clientId = 100L;
        dish.setIsActive(false);

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId,restaurant.getId())).thenReturn(false);
        when(dishPersistencePort.getDishById(orderDish.getDish().getId())).thenReturn(dish);

        assertThrows(DishNotAvailableException.class, () -> orderUseCase.createOrder(order));
        verify(dishPersistencePort, times(1)).getDishById(orderDish.getDish().getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .getRestaurantById(order.getRestaurant().getId());
        verify(orderPersistencePort, times(1))
                .hasPendingOrInProgressOrder(clientId,restaurant.getId());
    }

    @Test
    void getOrdersByStatus_ShouldReturnPaginatedOrders_WhenEmployeeIsAssociatedWithRestaurant() {
        String status = "PENDING";
        int page = 0;
        int size = 10;
        Long authenticatedEmployeeId = 100L;
        Long restaurantId = 1L;

        Paginated<Order> paginatedOrders = new Paginated<>(
                List.of(order),
                page,
                size,
                1L,
                1
        );

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(authenticatedEmployeeId);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(authenticatedEmployeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.getOrdersByStatus(restaurantId, status, page, size)).thenReturn(paginatedOrders);

        Paginated<Order> result = orderUseCase.getOrdersByStatus(status, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(paginatedOrders.getContent(), result.getContent());
        assertEquals(paginatedOrders.getPageNumber(), result.getPageNumber());
        assertEquals(paginatedOrders.getPageSize(), result.getPageSize());
        assertEquals(paginatedOrders.getTotalElements(), result.getTotalElements());
        assertEquals(paginatedOrders.getTotalPages(), result.getTotalPages());

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .getRestaurantByEmployeeId(authenticatedEmployeeId);
        verify(orderPersistencePort, times(1))
                .getOrdersByStatus(restaurantId, status, page, size);
    }

    @Test
    void getOrdersByStatus_ShouldThrowUnauthorizedException_WhenEmployeeNotAssociatedWithRestaurant() {
        String status = "PENDING";
        int page = 0;
        int size = 10;
        Long authenticatedEmployeeId = 100L;

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(authenticatedEmployeeId);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(authenticatedEmployeeId)).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> orderUseCase.getOrdersByStatus(status, page, size));

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .getRestaurantByEmployeeId(authenticatedEmployeeId);
        verifyNoInteractions(orderPersistencePort);
    }

    @Test
    void getOrdersByStatus_ShouldThrowInvalidPaginationException_WhenPaginationIsInvalid() {
        String status = "PENDING";
        Long authenticatedEmployeeId = 100L;
        Long restaurantId = 1L;

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(authenticatedEmployeeId);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(authenticatedEmployeeId)).thenReturn(restaurantId);

        assertThrows(InvalidPaginationException.class, () -> orderUseCase.getOrdersByStatus(status, -1, 10));
        assertThrows(InvalidPaginationException.class, () -> orderUseCase.getOrdersByStatus(status, 0, 0));

        verify(authenticationSecurityPort, times(2))
                .getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(2))
                .getRestaurantByEmployeeId(authenticatedEmployeeId);
        verifyNoInteractions(orderPersistencePort);
    }

    @Test
    void assignOrder_ShouldAssignOrderSuccessfully_WhenValidOrderIdIsProvided() {
        Long orderId = 1L;
        Long employeeId = 100L;
        Long restaurantId = 1L;

        order.setId(orderId);
        order.setChefId(null);
        order.setRestaurant(restaurant);

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.isOrderAssignedToEmployee(orderId, employeeId)).thenReturn(false);
        when(orderPersistencePort.updateOrder(order)).thenReturn(order);

        Order result = orderUseCase.assignOrder(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(employeeId, result.getChefId());
        assertNotNull(result.getDate());

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(orderPersistencePort, times(1)).getOrderById(orderId);
        verify(restaurantPersistencePort, times(1)).getRestaurantByEmployeeId(employeeId);
        verify(orderPersistencePort, times(1)).isOrderAssignedToEmployee(orderId, employeeId);
        verify(orderPersistencePort, times(1)).updateOrder(order);
    }


    @Test
    void assignOrder_ShouldThrowOrderNotFoundException_WhenOrderDoesNotBelongToEmployeeRestaurant() {
        Long orderId = 1L;
        Long employeeId = 100L;
        Long differentRestaurantId = 2L;

        order.setId(orderId);
        order.setRestaurant(restaurant);

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(employeeId)).thenReturn(differentRestaurantId);

        assertThrows(OrderNotFoundException.class, () -> orderUseCase.assignOrder(orderId));

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(orderPersistencePort, times(1)).getOrderById(orderId);
        verify(restaurantPersistencePort, times(1)).getRestaurantByEmployeeId(employeeId);
        verifyNoMoreInteractions(orderPersistencePort);
    }

    @Test
    void assignOrder_ShouldThrowOrderAlreadyAssignedException_WhenOrderIsAssignedToAnotherEmployee() {
        Long orderId = 1L;
        Long employeeId = 100L;
        Long anotherEmployeeId = 200L;

        order.setId(orderId);
        order.setChefId(anotherEmployeeId);
        order.setRestaurant(restaurant);

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(employeeId)).thenReturn(restaurant.getId());

        assertThrows(OrderAlreadyAssignedException.class, () -> orderUseCase.assignOrder(orderId));

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(orderPersistencePort, times(1)).getOrderById(orderId);
        verify(restaurantPersistencePort, times(1)).getRestaurantByEmployeeId(employeeId);
        verifyNoMoreInteractions(orderPersistencePort);
    }

    @Test
    void assignOrder_ShouldThrowOrderAlreadyAssignedException_WhenOrderIsAlreadyAssignedToSameEmployee() {
        Long orderId = 1L;
        Long employeeId = 100L;

        order.setId(orderId);
        order.setChefId(employeeId);
        order.setRestaurant(restaurant);

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(employeeId)).thenReturn(restaurant.getId());
        when(orderPersistencePort.isOrderAssignedToEmployee(orderId, employeeId)).thenReturn(true);

        assertThrows(OrderAlreadyAssignedException.class, () -> orderUseCase.assignOrder(orderId));

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(orderPersistencePort, times(1)).getOrderById(orderId);
        verify(restaurantPersistencePort, times(1)).getRestaurantByEmployeeId(employeeId);
        verify(orderPersistencePort, times(1)).isOrderAssignedToEmployee(orderId, employeeId);
        verifyNoMoreInteractions(orderPersistencePort);
    }

    @Test
    void orderReady_Success() {
        Long orderId = 1L;
        Long employeeId = 10L;
        Long restaurantId = 100L;
        int securityCode = 1234567;

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(restaurantId);

        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(DomainConstants.STATUS_IN_PREPARATION);
        mockOrder.setSecurityCode(securityCode);
        mockOrder.setRestaurant(restaurant1);

        MessageModel expectedMessageModel = new MessageModel();
        expectedMessageModel.setMessage("hola");

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(restaurantPersistencePort.getRestaurantByEmployeeId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(mockOrder);

        when(messageFeignPersistencePort.sendWhatsAppMessage(any(MessageModel.class)))
                .thenReturn(expectedMessageModel);

        MessageModel result = orderUseCase.orderReady(orderId);

        assertEquals(DomainConstants.STATUS_READY, mockOrder.getStatus());
        assertNotNull(mockOrder.getDate());
        verify(orderPersistencePort,times(1)).updateOrder(mockOrder);
        verify(messageFeignPersistencePort,(times(1)))
                .sendWhatsAppMessage(any(MessageModel.class));
        assertEquals(expectedMessageModel.getMessage(), result.getMessage());
    }

    @Test
    void orderDelivery_ShouldUpdateOrder_WhenSecurityCodeIsValidAndOrderIsReady() {
        Long orderId = 1L;
        Integer securityCode = 1234567;

        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(DomainConstants.STATUS_READY);
        mockOrder.setSecurityCode(securityCode);

        when(orderPersistencePort.getOrderById(orderId)).thenReturn(mockOrder);
        when(orderPersistencePort.updateOrder(any(Order.class))).thenReturn(mockOrder);

        Order result = orderUseCase.orderDelivery(orderId, securityCode);

        assertEquals(DomainConstants.STATUS_DELIVERY, result.getStatus());
        assertNotNull(result.getDate());

        verify(orderPersistencePort, times(1)).getOrderById(orderId);
        verify(orderPersistencePort, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void cancelOrder_ShouldSaveOrderStatusHistory_WhenCancellationIsSuccessful() {
        Long orderId = 1L;
        Long clientId = 10L;

        order.setId(orderId);
        order.setStatus(DomainConstants.STATUS_PENDING);

        restaurant.setId(2L);

        order.setRestaurant(restaurant);

        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.isAnOrderInProcessPending(clientId, restaurant.getId())).thenReturn(true);
        when(orderPersistencePort.updateOrder(any(Order.class))).thenReturn(order);

        Order canceledOrder = orderUseCase.cancelOrder(orderId);

        assertNotNull(canceledOrder);
        assertEquals(DomainConstants.STATUS_CANCELED, canceledOrder.getStatus());
        verify(orderPersistencePort).updateOrder(order);
        verify(orderStatusHistoryFeignPersistencePort).saveOrderStatusHistory(eq(order), any(OrderStatusHistory.class));
    }

    @Test
    void cancelOrder_OrderNotInProgress_ShouldThrowException() {
        Long orderId = 1L;
        Long clientId = 10L;

        order.setId(orderId);
        order.setStatus(DomainConstants.STATUS_PENDING);

        restaurant.setId(2L);

        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.isAnOrderInProcessPending(clientId, restaurant.getId())).thenReturn(false);

        assertThrows(OrderInProgressException.class, () -> orderUseCase.cancelOrder(orderId));
    }
}


