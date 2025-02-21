package small_square_microservice.small_square.application.handler.dishhandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.mapper.dishmapper.IDishMapper;
import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.model.Dish;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        dishRequest = new DishRequest();
        dish = new Dish();
        createdDish = new Dish();
        dishResponse = new DishResponse();
    }

    @Test
    void createDish_ShouldReturnDishResponse_WhenValidRequest() {

        when(dishMapper.toModel(dishRequest)).thenReturn(dish);
        when(dishServicePort.createDish(dish)).thenReturn(createdDish);
        when(dishMapper.toResponse(createdDish)).thenReturn(dishResponse);

        DishResponse result = dishHandler.createDish(dishRequest);

        assertNotNull(result);
        assertEquals(dishResponse, result);
        verify(dishMapper).toModel(dishRequest);
        verify(dishServicePort).createDish(dish);
        verify(dishMapper).toResponse(createdDish);
    }
}
