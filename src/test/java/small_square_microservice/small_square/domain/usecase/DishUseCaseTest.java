package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.exception.InvalidPaginationException;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.exception.UserIsNotOwnerException;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.ICategoryPersistencePort;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IAuthenticationSecurityPort authenticationSecurityPort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private final Long ownerId = 10L;
    private final Long otherOwnerId = 20L;
    private Restaurant restaurant;
    private Category category;
    private Dish dish;
    private Paginated<Dish> paginatedDishes;

    @BeforeEach
    void setupRestaurantAndCategory() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

        category = new Category();
        category.setId(2L);

        dish = new Dish();
        dish.setId(1L);
        dish.setRestaurant(restaurant);
        dish.setCategory(category);

        List<Dish> dishList = List.of(dish);
        paginatedDishes = new Paginated<>(dishList, 0, 10, 1L, 1);
    }

    @Test
    void createDish_ShouldReturnDish_WhenRestaurantCategoryExistAndUserIsOwner() {
        restaurant.setOwnerId(ownerId);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(ownerId);
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(categoryPersistencePort.getCategoryById(2L)).thenReturn(category);
        when(dishPersistencePort.createDish(any(Dish.class))).thenReturn(dish);

        Dish result = dishUseCase.createDish(dish);

        assertNotNull(result);
        verify(restaurantPersistencePort, times(1)).getRestaurantById(1L);
        verify(categoryPersistencePort, times(1)).getCategoryById(2L);
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(dishPersistencePort, times(1)).createDish(dish);
    }

    @Test
    void createDish_ShouldThrowException_WhenUserIsNotOwner() {
        restaurant.setOwnerId(otherOwnerId);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(ownerId);
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(categoryPersistencePort.getCategoryById(2L)).thenReturn(category);

        assertThrows(UserIsNotOwnerException.class, () -> dishUseCase.createDish(dish));

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1)).getRestaurantById(1L);
        verify(categoryPersistencePort, times(1)).getCategoryById(2L);
    }

    @Test
    void updatedDishById_ShouldThrowException_WhenUserIsNotOwner() {
        restaurant.setOwnerId(otherOwnerId);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(ownerId);
        when(dishPersistencePort.getDishById(1L)).thenReturn(dish);
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);

        Dish updatedDish = new Dish();
        updatedDish.setDescription("New Description");
        updatedDish.setPrice(1500.0);

        assertThrows(UserIsNotOwnerException.class, () -> dishUseCase.updatedDishById(1L, updatedDish));

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(dishPersistencePort, times(1)).getDishById(1L);
        verify(restaurantPersistencePort, times(1)).getRestaurantById(1L);
    }

    @Test
    void toggleDishStatus_ShouldThrowException_WhenUserIsNotOwner() {
        restaurant.setOwnerId(otherOwnerId);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(ownerId);
        when(dishPersistencePort.getDishById(1L)).thenReturn(dish);
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);

        assertThrows(UserIsNotOwnerException.class, () -> dishUseCase.toggleDishStatus(1L));

        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
        verify(dishPersistencePort, times(1)).getDishById(1L);
        verify(restaurantPersistencePort, times(1)).getRestaurantById(1L);
    }

    @Test
    void getDishesByRestaurant_ShouldReturnPaginatedDishes_WhenRestaurantExistsAndValidPagination() {
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(dishPersistencePort
                .getDishesByRestaurant(1L, 0, 10, null)).thenReturn(paginatedDishes);

        Paginated<Dish> result = dishUseCase.getDishesByRestaurant(1L, 0, 10, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(restaurantPersistencePort, times(1)).getRestaurantById(1L);
        verify(dishPersistencePort, times(1))
                .getDishesByRestaurant(1L, 0, 10, null);
    }

    @Test
    void getDishesByRestaurant_ShouldThrowException_WhenRestaurantDoesNotExist() {
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(null);

        assertThrows(RestaurantNotFundException.class,
                () -> dishUseCase.getDishesByRestaurant(1L, 0, 10, null));

        verify(restaurantPersistencePort, times(1)).getRestaurantById(1L);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void getDishesByRestaurant_ShouldThrowException_WhenPaginationIsInvalid() {
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);

        assertThrows(InvalidPaginationException.class,
                () -> dishUseCase.getDishesByRestaurant(1L, -1, 10, null));
        assertThrows(InvalidPaginationException.class,
                () -> dishUseCase.getDishesByRestaurant(1L, 0, 0, null));

        verify(restaurantPersistencePort, times(2)).getRestaurantById(1L);
        verifyNoInteractions(dishPersistencePort);
    }
}


