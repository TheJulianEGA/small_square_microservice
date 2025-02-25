package small_square_microservice.small_square.application.mapper.restaurantmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.restaurantdto.RegisterEmployeeToRestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponseForPagination;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.model.RestaurantEmployee;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper implements IRestaurantMapper {

    @Override
    public Restaurant toModel(RestaurantRequest restaurantRequest) {
        return Restaurant.builder()
                .name(restaurantRequest.getName())
                .address(restaurantRequest.getAddress())
                .ownerId(restaurantRequest.getOwnerId())
                .phone(restaurantRequest.getPhone())
                .logoUrl(restaurantRequest.getLogoUrl())
                .nit(restaurantRequest.getNit())
                .build();
    }

    @Override
    public RestaurantResponse toResponse(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .ownerId(restaurant.getOwnerId())
                .phone(restaurant.getPhone())
                .logoUrl(restaurant.getLogoUrl())
                .nit(restaurant.getNit())
                .employeeIds(restaurant.getEmployeeIds()
                        .stream()
                        .map(RestaurantEmployee::getEmployeeId)
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    public RestaurantResponseForPagination toResponseForPagination(Restaurant restaurant) {
        return RestaurantResponseForPagination.builder()
                .name(restaurant.getName())
                .logoUrl(restaurant.getLogoUrl())
                .build();
    }

    @Override
    public Restaurant employeeToRestaurantToModel(RegisterEmployeeToRestaurantRequest request) {
        return Restaurant.builder()
                .employeeIds(request.getEmployeeIds()
                        .stream()
                        .map(employeeId -> new RestaurantEmployee(null, employeeId))
                        .collect(Collectors.toList()))
                .build();
    }
}

