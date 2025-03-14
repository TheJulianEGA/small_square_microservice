package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IDishPersistencePort {

    Dish createDish(Dish dish);

    Dish getDishById(Long id);

    Dish updateDish(Dish dish);

    Paginated<Dish> getDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId);
}
