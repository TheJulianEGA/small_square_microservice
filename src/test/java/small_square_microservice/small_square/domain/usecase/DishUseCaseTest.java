package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDish_ShouldReturnDish_WhenRestaurantExists() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);

        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.createDish(any(Dish.class))).thenReturn(dish);

        Dish result = dishUseCase.createDish(dish);

        assertNotNull(result);
        verify(restaurantPersistencePort).getRestaurantById(1L);
        verify(dishPersistencePort).createDish(dish);
    }

    @Test
    void createDish_ShouldThrowException_WhenRestaurantDoesNotExist() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);
        when(restaurantPersistencePort.getRestaurantById(1L))
                .thenThrow(new RestaurantNotFundException(DomainConstants.USER_IS_NOT_OWNER));

        assertThrows(RestaurantNotFundException.class, () -> dishUseCase.createDish(dish));
        verify(restaurantPersistencePort).getRestaurantById(1L);
        verify(dishPersistencePort, never()).createDish(any(Dish.class));
    }
}