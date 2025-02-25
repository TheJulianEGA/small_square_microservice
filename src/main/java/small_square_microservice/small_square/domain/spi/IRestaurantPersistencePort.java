package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IRestaurantPersistencePort {

    Restaurant registerRestaurant (Restaurant restaurant);

    Restaurant getRestaurantById(Long id);

    Paginated<Restaurant> getAllRestaurants(int page, int size);

    Restaurant updateRestaurant(Restaurant restaurantToUpdate);

    Long findRestaurantByEmployeeId(Long employeeId);
}
