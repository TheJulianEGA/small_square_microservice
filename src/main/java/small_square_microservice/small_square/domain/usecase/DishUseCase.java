package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.exception.CategoryNotFundException;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.ICategoryPersistencePort;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;


public class DishUseCase implements IDishServicePort {


    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;

    public DishUseCase(
            IDishPersistencePort dishPersistencePort,
            IRestaurantPersistencePort restaurantPersistencePort,
            ICategoryPersistencePort categoryPersistencePort)
    {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public Dish createDish(Dish dish) {

        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(dish.getRestaurant().getId());

        if (restaurant == null) {
            throw new RestaurantNotFundException(DomainConstants.USER_IS_NOT_OWNER);
        }
        dish.setRestaurant(restaurant);

        Category category = categoryPersistencePort.getCategoryById(dish.getCategory().getId());
        if (category == null) {
            throw new CategoryNotFundException(DomainConstants.CATEGORY_NOT_FOUND);
        }
        dish.setCategory(category);


        return dishPersistencePort.createDish(dish);
    }
}
