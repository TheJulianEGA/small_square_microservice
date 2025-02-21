package small_square_microservice.small_square.application.handler.restauranthandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.mapper.restaurantmapper.IRestaurantMapper;
import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.model.Restaurant;

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
}
