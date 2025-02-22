package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.exception.UserIsNotOwnerException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserFeignPersistencePort userFeignPersistencePort;


    @Test
    void registerRestaurant_ShouldReturnRegisteredRestaurant_WhenOwnerIsValid() {

        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(1L);

        Restaurant registeredRestaurant = new Restaurant();
        registeredRestaurant.setId(100L);

        when(userFeignPersistencePort.existsUserWithOwnerRole(restaurant.getOwnerId())).thenReturn(true);
        when(restaurantPersistencePort.registerRestaurant(restaurant)).thenReturn(registeredRestaurant);

        Restaurant result = restaurantUseCase.registerRestaurant(restaurant);

        assertNotNull(result);
        assertEquals(registeredRestaurant.getId(), result.getId());
        verify(userFeignPersistencePort).existsUserWithOwnerRole(restaurant.getOwnerId());
        verify(restaurantPersistencePort).registerRestaurant(restaurant);
    }

    @Test
    void registerRestaurant_ShouldThrowException_WhenOwnerIsNotValid() {

        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(12L);

        when(userFeignPersistencePort.existsUserWithOwnerRole(restaurant.getOwnerId())).thenReturn(false);

        UserIsNotOwnerException exception = assertThrows(UserIsNotOwnerException.class,
                () -> restaurantUseCase.registerRestaurant(restaurant));

        assertEquals(DomainConstants.USER_IS_NOT_OWNER, exception.getMessage());
        verify(userFeignPersistencePort).existsUserWithOwnerRole(restaurant.getOwnerId());
        verify(restaurantPersistencePort, never()).registerRestaurant(any());
    }
}