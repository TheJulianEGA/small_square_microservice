package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;


public class DishUseCase implements IDishServicePort {


    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(
            IDishPersistencePort dishPersistencePort,
            IRestaurantPersistencePort restaurantPersistencePort)
    {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public Dish createDish(Dish dish) {

        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(dish.getRestaurant().getId());

        if (restaurant == null) {
            throw new RestaurantNotFundException(DomainConstants.USER_IS_NOT_OWNER);
        }
        dish.setRestaurant(restaurant);


        return dishPersistencePort.createDish(dish);
    }
}
