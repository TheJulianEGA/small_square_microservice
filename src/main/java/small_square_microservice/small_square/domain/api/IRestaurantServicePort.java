package small_square_microservice.small_square.domain.api;

import small_square_microservice.small_square.domain.model.Restaurant;

public interface IRestaurantServicePort {

    Restaurant  registerRestaurant (Restaurant restaurant);


}
