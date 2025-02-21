package small_square_microservice.small_square.domain.api;

import small_square_microservice.small_square.domain.model.Dish;

public interface IDishServicePort {

    Dish createDish(Dish dish);

}
