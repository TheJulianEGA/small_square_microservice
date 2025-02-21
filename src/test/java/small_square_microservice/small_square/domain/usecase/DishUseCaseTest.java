package small_square_microservice.small_square.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import small_square_microservice.small_square.domain.exception.CategoryNotFundException;
import small_square_microservice.small_square.domain.exception.DishNotFundException;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.ICategoryPersistencePort;
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

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDish_ShouldReturnDish_WhenRestaurantAndCategoryExist() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category category = new Category();
        category.setId(2L);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);
        dish.setCategory(category);

        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(categoryPersistencePort.getCategoryById(2L)).thenReturn(category);
        when(dishPersistencePort.createDish(any(Dish.class))).thenReturn(dish);

        Dish result = dishUseCase.createDish(dish);

        assertNotNull(result);
        verify(restaurantPersistencePort).getRestaurantById(1L);
        verify(categoryPersistencePort).getCategoryById(2L);
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
        verify(categoryPersistencePort, never()).getCategoryById(anyLong());
        verify(dishPersistencePort, never()).createDish(any(Dish.class));
    }

    @Test
    void createDish_ShouldThrowException_WhenCategoryDoesNotExist() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category category = new Category();
        category.setId(2L);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);
        dish.setCategory(category);

        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(categoryPersistencePort.getCategoryById(2L))
                .thenThrow(new CategoryNotFundException(DomainConstants.CATEGORY_NOT_FOUND));

        assertThrows(CategoryNotFundException.class, () -> dishUseCase.createDish(dish));
        verify(restaurantPersistencePort).getRestaurantById(1L);
        verify(categoryPersistencePort).getCategoryById(2L);
        verify(dishPersistencePort, never()).createDish(any(Dish.class));
    }

    @Test
    void updatedDishById_ShouldReturnUpdatedDish_WhenDishExists() {
        Long dishId = 1L;
        Dish existingDish = new Dish();
        existingDish.setId(dishId);
        existingDish.setDescription("Old Description");
        existingDish.setPrice(1000.0);

        Dish updatedDish = new Dish();
        updatedDish.setDescription("New Description");
        updatedDish.setPrice(1500.0);

        when(dishPersistencePort.getDishById(dishId)).thenReturn(existingDish);
        when(dishPersistencePort.updateDish(any(Dish.class))).thenReturn(existingDish);

        Dish result = dishUseCase.updatedDishById(dishId, updatedDish);

        assertNotNull(result);
        assertEquals("New Description", result.getDescription());
        assertEquals(1500.0, result.getPrice());
        verify(dishPersistencePort).getDishById(dishId);
        verify(dishPersistencePort).updateDish(existingDish);
    }

    @Test
    void updatedDishById_ShouldThrowException_WhenDishDoesNotExist() {
        Long dishId = 1L;
        Dish updatedDish = new Dish();
        updatedDish.setDescription("New Description");
        updatedDish.setPrice(1500.0);

        when(dishPersistencePort.getDishById(dishId))
                .thenThrow(new DishNotFundException(DomainConstants.DISH_NOT_FOUND));

        assertThrows(DishNotFundException.class, () -> dishUseCase.updatedDishById(dishId, updatedDish));
        verify(dishPersistencePort).getDishById(dishId);
        verify(dishPersistencePort, never()).updateDish(any(Dish.class));
    }
}