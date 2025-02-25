package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.exception.InvalidPaginationException;
import small_square_microservice.small_square.domain.exception.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.exception.UserIsNotOwnerException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.Arrays;
import java.util.List;

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

    @Mock
    private IAuthenticationSecurityPort authenticationSecurityPort;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwnerId(100L);
        restaurant.setEmployeeIds(List.of(200L, 201L));
    }

    @Test
    void registerRestaurant_ShouldReturnRegisteredRestaurant_WhenOwnerIsValid() {
        when(userFeignPersistencePort.existsUserWithOwnerRole(restaurant.getOwnerId())).thenReturn(true);
        when(restaurantPersistencePort.registerRestaurant(restaurant)).thenReturn(restaurant);

        Restaurant result = restaurantUseCase.registerRestaurant(restaurant);

        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        verify(userFeignPersistencePort).existsUserWithOwnerRole(restaurant.getOwnerId());
        verify(restaurantPersistencePort).registerRestaurant(restaurant);
    }

    @Test
    void registerRestaurant_ShouldThrowException_WhenOwnerIsNotValid() {
        when(userFeignPersistencePort.existsUserWithOwnerRole(restaurant.getOwnerId())).thenReturn(false);

        UserIsNotOwnerException exception = assertThrows(UserIsNotOwnerException.class,
                () -> restaurantUseCase.registerRestaurant(restaurant));

        assertEquals(DomainConstants.USER_IS_NOT_OWNER, exception.getMessage());
        verify(userFeignPersistencePort).existsUserWithOwnerRole(restaurant.getOwnerId());
        verify(restaurantPersistencePort, never()).registerRestaurant(any());
    }

    @Test
    void getAllRestaurants_ShouldReturnPaginatedRestaurants_WhenValidPageAndSize() {
        int page = 0, size = 10;
        List<Restaurant> restaurants = List.of(new Restaurant(), new Restaurant());
        Paginated<Restaurant> paginatedRestaurants = new Paginated<>(restaurants, page, size, 2, 1);

        when(restaurantPersistencePort.getAllRestaurants(page, size)).thenReturn(paginatedRestaurants);

        Paginated<Restaurant> result = restaurantUseCase.getAllRestaurants(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(restaurants.size(), result.getContent().size());
        verify(restaurantPersistencePort).getAllRestaurants(page, size);
    }

    @Test
    void getAllRestaurants_ShouldThrowException_WhenInvalidPageOrSize() {
        assertThrows(InvalidPaginationException.class, () -> restaurantUseCase.getAllRestaurants(-1, 10));
        assertThrows(InvalidPaginationException.class, () -> restaurantUseCase.getAllRestaurants(0, 0));

        verify(restaurantPersistencePort, never()).getAllRestaurants(anyInt(), anyInt());
    }

    @Test
    void updateRestaurantEmployees_ShouldUpdateSuccessfully_WhenOwnerHasPermission() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(restaurant.getOwnerId());
        when(userFeignPersistencePort.existsUserWithEmployeeRole(200L)).thenReturn(true);
        when(userFeignPersistencePort.existsUserWithEmployeeRole(201L)).thenReturn(true);
        when(restaurantPersistencePort.updateRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant updatedRestaurant = restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), restaurant);

        assertNotNull(updatedRestaurant);
        assertEquals(restaurant.getEmployeeIds().size(), updatedRestaurant.getEmployeeIds().size());
        verify(restaurantPersistencePort).updateRestaurant(any(Restaurant.class));
    }

    @Test
    void updateRestaurantEmployees_ShouldThrowException_WhenRestaurantDoesNotExist() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), restaurant));

        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
    }

    @Test
    void updateRestaurantEmployees_ShouldThrowException_WhenUserIsNotOwner() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(999L);

        assertThrows(UserIsNotOwnerException.class,
                () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), restaurant));

        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
    }

    @Test
    void updateRestaurantEmployees_ShouldThrowException_WhenEmployeeIsNotValid() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(restaurant.getOwnerId());
        when(userFeignPersistencePort.existsUserWithEmployeeRole(200L)).thenReturn(false);

        assertThrows(UserIsNotOwnerException.class,
                () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), restaurant));

        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
    }
}