package small_square_microservice.small_square.application.handler.restauranthandler;

import small_square_microservice.small_square.application.dto.restaurantdto.RegisterEmployeeToRestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponseForPagination;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IRestaurantHandler {

    RestaurantResponse registerRestaurant(RestaurantRequest restaurantRequest);

    Paginated<RestaurantResponseForPagination> getAllRestaurants(int page, int size);

    RestaurantResponse updateRestaurantEmployees(Long restaurantId, RegisterEmployeeToRestaurantRequest request);
}
