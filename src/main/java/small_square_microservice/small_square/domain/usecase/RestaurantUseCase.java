package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.exception.InvalidPaginationException;
import small_square_microservice.small_square.domain.exception.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.exception.UserIsNotOwnerException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.domain.util.DomainConstants;
import small_square_microservice.small_square.domain.util.Paginated;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserFeignPersistencePort userFeignPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                             IUserFeignPersistencePort userFeignPersistencePort,
                             IAuthenticationSecurityPort authenticationSecurityPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignPersistencePort = userFeignPersistencePort;
        this.authenticationSecurityPort = authenticationSecurityPort;
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

    @Override
    public Restaurant updateRestaurantEmployees(Long restaurantId, Restaurant restaurant) {

        Restaurant restaurantToUpdate = validateRestaurantExists(restaurantId);

        validateOwnerPermission(restaurantToUpdate);

        for (Long employeeId : restaurant.getEmployeeIds()) {
            if (!userFeignPersistencePort.existsUserWithEmployeeRole(employeeId)) {
                throw new UserIsNotOwnerException(DomainConstants.USER_IS_NOT_EMPLOYEE);
            }
        }

        restaurantToUpdate.setEmployeeIds(restaurant.getEmployeeIds());

        return restaurantPersistencePort.updateRestaurant(restaurantToUpdate);
    }


    private void validateOwnerPermission(Restaurant restaurant) {

        Long ownerId = authenticationSecurityPort.getAuthenticatedUserId();

        if (!ownerId.equals(restaurant.getOwnerId())) {
            throw new UserIsNotOwnerException(DomainConstants.USER_IS_NOT_OWNER_OF_THIS_RESTAURANT);
        }
    }


    private Restaurant validateRestaurantExists(Long id) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(id);
        if (restaurant == null) {
            throw new RestaurantNotFoundException(DomainConstants.RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }

}
