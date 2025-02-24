package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.exception.*;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.ICategoryPersistencePort;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;


public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;

    public DishUseCase(
            IDishPersistencePort dishPersistencePort,
            IRestaurantPersistencePort restaurantPersistencePort,
            ICategoryPersistencePort categoryPersistencePort,
            IAuthenticationSecurityPort authenticationSecurityPort)
    {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.authenticationSecurityPort = authenticationSecurityPort;
    }

    @Override
    public Dish createDish(Dish dish) {

        dish.setRestaurant(validateRestaurantReference(dish));
        dish.setCategory(validateCategoryReference(dish));

        validateOwnerPermission(dish);

        return dishPersistencePort.createDish(dish);
    }

    @Override
    public Dish updatedDishById(Long id, Dish dish) {

        Dish dishToUpdate = validateDishExists(id);

        dishToUpdate.setRestaurant(validateRestaurantReference(dishToUpdate));

        validateOwnerPermission(dishToUpdate);

        dishToUpdate.setDescription(dish.getDescription());
        dishToUpdate.setPrice(dish.getPrice());

        return dishPersistencePort.updateDish(dishToUpdate);
    }

    @Override
    public Dish toggleDishStatus(Long id) {
        Dish dish = validateDishExists(id);

        dish.setRestaurant(validateRestaurantReference(dish));

        validateOwnerPermission(dish);

        dish.setIsActive(!dish.getIsActive());

        return dishPersistencePort.updateDish(dish);
    }

    @Override
    public Paginated<Dish> getDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId) {

        if (restaurantPersistencePort.getRestaurantById(restaurantId) == null) {
            throw new RestaurantNotFundException(DomainConstants.RESTAURANT_NOT_FOUND);
        }

        if ( page < 0 || size < 1) {
            throw new InvalidPaginationException(DomainConstants.INVALID_PAGINATION_MESSAGE);
        }
        return dishPersistencePort.getDishesByRestaurant(restaurantId, page, size, categoryId);
    }


    private Dish validateDishExists(Long id) {
        Dish dish = dishPersistencePort.getDishById(id);
        if (dish == null) {
            throw new DishNotFundException(DomainConstants.DISH_NOT_FOUND);
        }
        return dish;
    }

    private void validateOwnerPermission(Dish dish) {

        Long ownerId = authenticationSecurityPort.getAuthenticatedUserId();

        if (!ownerId.equals(dish.getRestaurant().getOwnerId())) {
            throw new UserIsNotOwnerException(DomainConstants.USER_IS_NOT_OWNER_OF_THIS_RESTAURANT);
        }
    }

    private Restaurant validateRestaurantReference(Dish dish) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(dish.getRestaurant().getId());

        if (restaurant == null) {
            throw new RestaurantNotFundException(DomainConstants.RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }

    private Category validateCategoryReference(Dish dish) {
        Category category = categoryPersistencePort.getCategoryById(dish.getCategory().getId());
        if (category == null) {
            throw new CategoryNotFundException(DomainConstants.CATEGORY_NOT_FOUND);
        }
        return category;
    }


}
