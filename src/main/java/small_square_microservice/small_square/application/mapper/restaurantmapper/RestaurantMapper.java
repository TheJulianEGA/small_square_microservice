package small_square_microservice.small_square.application.mapper.restaurantmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.restaurantdto.RegisterEmployeeToRestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponseForPagination;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.Restaurant;

import java.util.List;

@Component
public class RestaurantMapper implements IRestaurantMapper {

    @Override
    public Restaurant toModel(RestaurantRequest restaurantRequest) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantRequest.getName());
        restaurant.setAddress(restaurantRequest.getAddress());
        restaurant.setOwnerId(restaurantRequest.getOwnerId());
        restaurant.setPhone(restaurantRequest.getPhone());
        restaurant.setLogoUrl(restaurantRequest.getLogoUrl());
        restaurant.setNit(restaurantRequest.getNit());
        return restaurant;
    }

    @Override
    public RestaurantResponse toResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setAddress(restaurant.getAddress());
        response.setOwnerId(restaurant.getOwnerId());
        response.setPhone(restaurant.getPhone());
        response.setLogoUrl(restaurant.getLogoUrl());
        response.setNit(restaurant.getNit());
        response.setEmployeeIds(restaurant.getEmployeeIds());
        return response;
    }

    @Override
    public RestaurantResponseForPagination toResponseForPagination(Restaurant restaurant) {
        RestaurantResponseForPagination response = new RestaurantResponseForPagination();
        response.setName(restaurant.getName());
        response.setLogoUrl(restaurant.getLogoUrl());
        return response;
    }

    @Override
    public Restaurant employeeToRestaurantToModel( RegisterEmployeeToRestaurantRequest request) {
            Restaurant restaurant = new Restaurant();
        restaurant.setEmployeeIds(request.getEmployeeIds());
        return restaurant;

    }


}
