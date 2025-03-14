package small_square_microservice.small_square.domain.api;

import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IDishServicePort {

    Dish createDish(Dish dish);

    Dish updatedDishById(Long id, Dish dish);

    Dish toggleDishStatus(Long id);

    Paginated<Dish> getDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId);
}
