package small_square_microservice.small_square.application.handler.restauranthandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.mapper.restaurantmapper.IRestaurantMapper;
import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.model.Restaurant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantHandlerTest {

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Mock
    private IRestaurantMapper restaurantMapper;

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerRestaurant_ShouldReturnRestaurantResponse_WhenValidRequest() {

        RestaurantRequest restaurantRequest = new RestaurantRequest();
        Restaurant restaurant = new Restaurant();
        Restaurant registeredRestaurant = new Restaurant();
        RestaurantResponse expectedResponse = new RestaurantResponse();

        when(restaurantMapper.toModel(restaurantRequest)).thenReturn(restaurant);
        when(restaurantServicePort.registerRestaurant(restaurant)).thenReturn(registeredRestaurant);
        when(restaurantMapper.toResponse(registeredRestaurant)).thenReturn(expectedResponse);

        RestaurantResponse actualResponse = restaurantHandler.registerRestaurant(restaurantRequest);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(restaurantMapper, times(1)).toModel(restaurantRequest);
        verify(restaurantServicePort, times(1)).registerRestaurant(restaurant);
        verify(restaurantMapper, times(1)).toResponse(registeredRestaurant);
    }
}