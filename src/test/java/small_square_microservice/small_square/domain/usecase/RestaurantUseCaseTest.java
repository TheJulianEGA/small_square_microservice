package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.exception.*;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.model.RestaurantEmployee;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.ArrayList;
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
    private Restaurant updatedRestaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwnerId(100L);
        restaurant.setEmployeeIds(new ArrayList<>());

        updatedRestaurant = new Restaurant();
        updatedRestaurant.setId(1L);
        updatedRestaurant.setOwnerId(100L);
        updatedRestaurant.setEmployeeIds(new ArrayList<>(List.of(new RestaurantEmployee(1L, 200L))));
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
    void updateRestaurantEmployees_ShouldThrowException_WhenRestaurantDoesNotExist() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(null);

        Executable action = () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), updatedRestaurant);
        assertThrows(RestaurantNotFoundException.class, action);

        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
    }

    @Test
    void updateRestaurantEmployees_ShouldThrowException_WhenUserIsNotOwner() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(999L);

        Executable action = () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), updatedRestaurant);
        assertThrows(UserIsNotOwnerException.class, action);

        verify(restaurantPersistencePort, times(1)).getRestaurantById(restaurant.getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
    }

    @Test
    void updateRestaurantEmployees_ShouldThrowException_WhenEmployeeIsNotValid() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(restaurant.getOwnerId());
        when(userFeignPersistencePort.existsUserWithEmployeeRole(200L)).thenReturn(false);

        Executable action = () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), updatedRestaurant);
        assertThrows(UserIsNotOwnerException.class, action);

        verify(restaurantPersistencePort, times(1)).getRestaurantById(restaurant.getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(userFeignPersistencePort, times(1)).existsUserWithEmployeeRole(200L);
        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
    }

    @Test
    void updateRestaurantEmployees_ShouldThrowException_WhenEmployeeIsAlreadyInAnotherRestaurant() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(restaurant.getOwnerId());
        when(userFeignPersistencePort.existsUserWithEmployeeRole(200L)).thenReturn(true);
        when(restaurantPersistencePort.findRestaurantByEmployeeId(200L)).thenReturn(2L);

        Executable action = () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), updatedRestaurant);
        assertThrows(EmployeeAlreadyInAnotherRestaurantException.class, action);

        verify(restaurantPersistencePort, times(1)).getRestaurantById(restaurant.getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(userFeignPersistencePort, times(1)).existsUserWithEmployeeRole(200L);
        verify(restaurantPersistencePort, times(1)).findRestaurantByEmployeeId(200L);
        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
    }

    @Test
    void updateRestaurantEmployees_ShouldThrowException_WhenEmployeeIsAlreadyInSameRestaurant() {
        restaurant.getEmployeeIds().add(new RestaurantEmployee(1L, 200L));

        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(restaurant.getOwnerId());
        when(userFeignPersistencePort.existsUserWithEmployeeRole(200L)).thenReturn(true);
        when(restaurantPersistencePort.findRestaurantByEmployeeId(200L)).thenReturn(1L);

        Executable action = () -> restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), updatedRestaurant);
        assertThrows(EmployeeAlreadyInRestaurantException.class, action);

        verify(restaurantPersistencePort, never()).updateRestaurant(any(Restaurant.class));
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(userFeignPersistencePort, times(1)).existsUserWithEmployeeRole(200L);
        verify(restaurantPersistencePort, times(1)).findRestaurantByEmployeeId(200L);

    }

    @Test
    void updateRestaurantEmployees_ShouldUpdateRestaurant_WhenEmployeesAreValid() {
        when(restaurantPersistencePort.getRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(restaurant.getOwnerId());
        when(userFeignPersistencePort.existsUserWithEmployeeRole(200L)).thenReturn(true);
        when(restaurantPersistencePort.findRestaurantByEmployeeId(200L)).thenReturn(null);
        when(restaurantPersistencePort.updateRestaurant(any(Restaurant.class))).thenReturn(updatedRestaurant);

        Restaurant result = restaurantUseCase.updateRestaurantEmployees(restaurant.getId(), updatedRestaurant);

        assertNotNull(result);
        assertEquals(1, result.getEmployeeIds().size());
        verify(restaurantPersistencePort, times(1)).getRestaurantById(restaurant.getId());
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(userFeignPersistencePort, times(1)).existsUserWithEmployeeRole(200L);
        verify(restaurantPersistencePort, times(1)).findRestaurantByEmployeeId(200L);
        verify(restaurantPersistencePort, times(1)).updateRestaurant(any(Restaurant.class));
    }
}