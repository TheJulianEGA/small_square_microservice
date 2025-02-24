package small_square_microservice.small_square.domain.api;

import small_square_microservice.small_square.application.dto.restaurantdto.RegisterEmployeeToRestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IRestaurantServicePort {

    Restaurant  registerRestaurant (Restaurant restaurant);

    Paginated<Restaurant> getAllRestaurants(int page, int size);

    Restaurant updateRestaurantEmployees(Long restaurantId, Restaurant restaurant);
}
