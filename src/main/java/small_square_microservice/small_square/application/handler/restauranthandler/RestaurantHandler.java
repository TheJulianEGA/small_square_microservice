package small_square_microservice.small_square.application.handler.restauranthandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.application.dto.restaurantdto.RegisterEmployeeToRestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponseForPagination;
import small_square_microservice.small_square.application.mapper.restaurantmapper.IRestaurantMapper;
import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantMapper restaurantMapper;
    private final IRestaurantServicePort restaurantServicePort;

    @Override
    public RestaurantResponse registerRestaurant(RestaurantRequest restaurantRequest) {

        Restaurant restaurant = restaurantMapper.toModel(restaurantRequest);

        Restaurant registeredRestaurant = restaurantServicePort.registerRestaurant(restaurant);

        return restaurantMapper.toResponse(registeredRestaurant);
    }

    @Override
    public Paginated<RestaurantResponseForPagination> getAllRestaurants(int page, int size) {
        Paginated<Restaurant> paginatedRestaurants = restaurantServicePort.getAllRestaurants(page, size);

        List<RestaurantResponseForPagination> restaurantResponses = paginatedRestaurants.getContent()
                .stream()
                .map(restaurantMapper::toResponseForPagination)
                .toList();

        return new Paginated<>(
                restaurantResponses,
                paginatedRestaurants.getPageNumber(),
                paginatedRestaurants.getPageSize(),
                paginatedRestaurants.getTotalElements(),
                paginatedRestaurants.getTotalPages()
        );
    }

    @Override
    public RestaurantResponse updateRestaurantEmployees(Long restaurantId, RegisterEmployeeToRestaurantRequest request) {

        Restaurant restaurant = restaurantMapper.employeeToRestaurantToModel(request);

        Restaurant updateRestaurant = restaurantServicePort.updateRestaurantEmployees(restaurantId,restaurant);

        return restaurantMapper.toResponse(updateRestaurant);
    }

}
