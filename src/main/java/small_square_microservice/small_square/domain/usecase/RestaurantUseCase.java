package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.exception.UserIsNotOwnerException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;

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

}
