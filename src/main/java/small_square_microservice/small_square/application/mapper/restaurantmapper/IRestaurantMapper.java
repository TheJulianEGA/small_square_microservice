package small_square_microservice.small_square.application.mapper.restaurantmapper;

import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.domain.model.Restaurant;

public interface IRestaurantMapper {

    Restaurant toModel(RestaurantRequest restaurantRequest);

    RestaurantResponse toResponse(Restaurant restaurant);


}
