package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.exception.InvalidPaginationException;
import small_square_microservice.small_square.domain.exception.UserIsNotOwnerException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserFeignPersistencePort userFeignPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                             IUserFeignPersistencePort userFeignPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignPersistencePort = userFeignPersistencePort;
    }

    @Override
    public Restaurant registerRestaurant(Restaurant restaurant) {

        if (!userFeignPersistencePort.existsUserWithOwnerRole(restaurant.getOwnerId())){
            throw new UserIsNotOwnerException(DomainConstants.USER_IS_NOT_OWNER);
        }

        return restaurantPersistencePort.registerRestaurant(restaurant);
    }

    @Override
    public Paginated<Restaurant> getAllRestaurants(int page, int size) {
        if ( page < 0 || size < 1) {
            throw new InvalidPaginationException(DomainConstants.INVALID_PAGINATION_MESSAGE);
        }
        return restaurantPersistencePort.getAllRestaurants(page, size);
    }

}
