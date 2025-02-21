package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Restaurant;

public interface IRestaurantPersistencePort {

    Restaurant registerRestaurant (Restaurant restaurant);

    Restaurant getRestaurantById(Long id);
}
