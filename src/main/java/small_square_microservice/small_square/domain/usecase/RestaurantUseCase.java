package small_square_microservice.small_square.domain.usecase;

import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.exception.*;
import small_square_microservice.small_square.domain.exception.notfound.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.model.RestaurantEmployee;
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

        for (RestaurantEmployee employee : restaurant.getEmployeeIds()) {
            validateEmployeeRole(employee.getEmployeeId());
            validateEmployeeNotInAnotherRestaurant(employee.getEmployeeId(), restaurantId);
            validateEmployeeNotAlreadyInRestaurant(employee.getEmployeeId(), restaurantToUpdate);

        }

        restaurantToUpdate.getEmployeeIds().addAll(restaurant.getEmployeeIds());
        return restaurantPersistencePort.updateRestaurant(restaurantToUpdate);
    }


    private void validateEmployeeRole(Long employeeId) {
        if (!userFeignPersistencePort.existsUserWithEmployeeRole(employeeId)) {
            throw new UserIsNotOwnerException(DomainConstants.USER_IS_NOT_EMPLOYEE);
        }
    }

    private void validateEmployeeNotAlreadyInRestaurant(Long employeeId, Restaurant restaurantToUpdate) {
        boolean alreadyInRestaurant = restaurantToUpdate.getEmployeeIds()
                .stream()
                .map(RestaurantEmployee::getEmployeeId)
                .toList()
                .contains(employeeId);

        if (alreadyInRestaurant) {
            throw new EmployeeAlreadyInRestaurantException(DomainConstants.EMPLOYEE_ALREADY_IN_RESTAURANT);
        }
    }

    private void validateEmployeeNotInAnotherRestaurant(Long employeeId, Long restaurantId) {
        Long existingRestaurantId = restaurantPersistencePort.findRestaurantByEmployeeId(employeeId);
        if (existingRestaurantId != null && !existingRestaurantId.equals(restaurantId)) {
            throw new EmployeeAlreadyInAnotherRestaurantException(DomainConstants.EMPLOYEE_ALREADY_IN_ANOTHER_RESTAURANT);
        }
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
