package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.exception.DishNotAvailableException;
import small_square_microservice.small_square.domain.exception.DishNotFoundException;
import small_square_microservice.small_square.domain.exception.OrderInProgressException;
import small_square_microservice.small_square.domain.exception.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IOrderPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;

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
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId)).thenReturn(false);
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
        verify(orderPersistencePort, times(1)).hasPendingOrInProgressOrder(clientId);
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
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId)).thenReturn(true);

        assertThrows(OrderInProgressException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void createOrder_ShouldThrowException_WhenDishNotFound() {
        Long clientId = 100L;

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId)).thenReturn(false);
        when(dishPersistencePort.getDishById(orderDish.getDish().getId())).thenReturn(null);

        assertThrows(DishNotFoundException.class, () -> orderUseCase.createOrder(order));
        verify(dishPersistencePort, times(1)).getDishById(orderDish.getDish().getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .getRestaurantById(order.getRestaurant().getId());
        verify(orderPersistencePort, times(1)).hasPendingOrInProgressOrder(clientId);
    }

    @Test
    void createOrder_ShouldThrowException_WhenDishIsInactive() {
        Long clientId = 100L;
        dish.setIsActive(false);

        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(clientId);
        when(restaurantPersistencePort.getRestaurantById(order.getRestaurant().getId())).thenReturn(restaurant);
        when(orderPersistencePort.hasPendingOrInProgressOrder(clientId)).thenReturn(false);
        when(dishPersistencePort.getDishById(orderDish.getDish().getId())).thenReturn(dish);

        assertThrows(DishNotAvailableException.class, () -> orderUseCase.createOrder(order));
        verify(dishPersistencePort, times(1)).getDishById(orderDish.getDish().getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .getRestaurantById(order.getRestaurant().getId());
        verify(orderPersistencePort, times(1)).hasPendingOrInProgressOrder(clientId);
    }
}
