package small_square_microservice.small_square.application.handler.restauranthandler;

import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;

public interface IRestaurantHandler {

    RestaurantResponse registerRestaurant(RestaurantRequest restaurantRequest);

}
