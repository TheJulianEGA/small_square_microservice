package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Dish;

public interface IDishPersistencePort {

    Dish createDish(Dish dish);

}
