package small_square_microservice.small_square.application.handler.restauranthandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponseForPagination;
import small_square_microservice.small_square.application.mapper.restaurantmapper.IRestaurantMapper;
import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Mock
    private IRestaurantMapper restaurantMapper;

    @Mock
    private IRestaurantServicePort restaurantServicePort;


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

    @Test
    void getAllRestaurants_ShouldReturnPaginatedRestaurants() {

        int page = 0;
        int size = 10;

        List<Restaurant> restaurantList = List.of(new Restaurant(), new Restaurant());
        Paginated<Restaurant> paginatedRestaurants = new Paginated<>(restaurantList, page, size, 2L, 1);

        List<RestaurantResponseForPagination> restaurantResponses = List.of(
                new RestaurantResponseForPagination(),
                new RestaurantResponseForPagination()
        );

        when(restaurantServicePort.getAllRestaurants(page, size)).thenReturn(paginatedRestaurants);
        when(restaurantMapper.toResponseForPagination(any(Restaurant.class)))
                .thenReturn(restaurantResponses.get(0), restaurantResponses.get(1));

        Paginated<RestaurantResponseForPagination> result = restaurantHandler.getAllRestaurants(page, size);

        assertNotNull(result);
        assertEquals(restaurantResponses, result.getContent());
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(2L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        verify(restaurantServicePort, times(1)).getAllRestaurants(page, size);
        verify(restaurantMapper, times(restaurantList.size())).toResponseForPagination(any(Restaurant.class));
    }
}