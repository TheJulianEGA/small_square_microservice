package small_square_microservice.small_square.application.handler.dishhandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.application.mapper.dishmapper.IDishMapper;
import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class DishHandlerTest {

    @Mock
    private IDishServicePort dishServicePort;

    @Mock
    private IDishMapper dishMapper;

    @InjectMocks
    private DishHandler dishHandler;

    private DishRequest dishRequest;
    private Dish dish;
    private Dish createdDish;
    private DishResponse dishResponse;
    private DishUpdateRequest dishUpdateRequest;
    private Paginated<Dish> paginatedDishes;
    private Paginated<DishResponse> expectedPaginatedDishes;

    @BeforeEach
    void setUp() {
        dishRequest = new DishRequest();
        dish = new Dish();
        createdDish = new Dish();
        dishResponse = new DishResponse();
        dishUpdateRequest = new DishUpdateRequest();

        List<Dish> dishList = List.of(dish);
        List<DishResponse> dishResponseList = List.of(dishResponse);

        paginatedDishes = new Paginated<>(dishList, 0, 10, 1L, 1);
        expectedPaginatedDishes = new Paginated<>(dishResponseList, 0, 10, 1L, 1);
    }

    @Test
    void createDish_ShouldReturnDishResponse_WhenValidRequest() {
        when(dishMapper.toModel(dishRequest)).thenReturn(dish);
        when(dishServicePort.createDish(dish)).thenReturn(createdDish);
        when(dishMapper.toResponse(createdDish)).thenReturn(dishResponse);

        DishResponse result = dishHandler.createDish(dishRequest);

        assertNotNull(result);
        assertEquals(dishResponse, result);
        verify(dishMapper, times(1)).toModel(dishRequest);
        verify(dishServicePort, times(1)).createDish(dish);
        verify(dishMapper, times(1)).toResponse(createdDish);
    }

    @Test
    void updateDish_ShouldReturnDishResponse_WhenValidRequest() {
        Long dishId = 1L;
        Dish dishToUpdate = new Dish();
        Dish updatedDish = new Dish();
        DishResponse expectedResponse = new DishResponse();

        when(dishMapper.updatedDishRequestToModel(dishUpdateRequest)).thenReturn(dishToUpdate);
        when(dishServicePort.updatedDishById(dishId, dishToUpdate)).thenReturn(updatedDish);
        when(dishMapper.toResponse(updatedDish)).thenReturn(expectedResponse);

        DishResponse result = dishHandler.updateDish(dishId, dishUpdateRequest);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(dishMapper, times(1)).updatedDishRequestToModel(dishUpdateRequest);
        verify(dishServicePort, times(1)).updatedDishById(dishId, dishToUpdate);
        verify(dishMapper, times(1)).toResponse(updatedDish);
    }

    @Test
    void toggleDishStatus_ShouldReturnDishResponse_WhenValidId() {
        Long dishId = 1L;
        Dish toggledDish = new Dish();
        DishResponse expectedResponse = new DishResponse();

        when(dishServicePort.toggleDishStatus(dishId)).thenReturn(toggledDish);
        when(dishMapper.toResponse(toggledDish)).thenReturn(expectedResponse);

        DishResponse result = dishHandler.toggleDishStatus(dishId);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(dishServicePort, times(1)).toggleDishStatus(dishId);
        verify(dishMapper, times(1)).toResponse(toggledDish);
    }

    @Test
    void getDishesByRestaurant_ShouldReturnPaginatedDishResponses_WhenValidRequest() {
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;
        Long categoryId = 2L;

        when(dishServicePort.getDishesByRestaurant(restaurantId, page, size, categoryId))
                .thenReturn(paginatedDishes);
        when(dishMapper.toResponse(dish)).thenReturn(dishResponse);

        Paginated<DishResponse> result = dishHandler.getDishesByRestaurant(restaurantId, page, size, categoryId);

        assertNotNull(result);
        assertEquals(expectedPaginatedDishes.getContent(), result.getContent());
        assertEquals(expectedPaginatedDishes.getPageNumber(), result.getPageNumber());
        assertEquals(expectedPaginatedDishes.getPageSize(), result.getPageSize());
        assertEquals(expectedPaginatedDishes.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPaginatedDishes.getTotalPages(), result.getTotalPages());

        verify(dishServicePort, times(1)).getDishesByRestaurant(restaurantId, page, size, categoryId);
        verify(dishMapper, times(1)).toResponse(dish);
    }
}

