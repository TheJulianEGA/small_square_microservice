package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

}
