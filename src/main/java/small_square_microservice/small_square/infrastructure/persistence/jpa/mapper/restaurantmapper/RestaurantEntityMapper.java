package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;

@Component
public class RestaurantEntityMapper implements IRestaurantEntityMapper {

    @Override
    public RestaurantEntity toEntity(Restaurant restaurant) {
        return RestaurantEntity.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .ownerId(restaurant.getOwnerId())
                .phone(restaurant.getPhone())
                .logoUrl(restaurant.getLogoUrl())
                .nit(restaurant.getNit())
                .employeeIds(restaurant.getEmployeeIds())
                .build();
    }

    @Override
    public Restaurant toModel(RestaurantEntity entity) {
        return Restaurant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .ownerId(entity.getOwnerId())
                .phone(entity.getPhone())
                .logoUrl(entity.getLogoUrl())
                .nit(entity.getNit())
                .employeeIds(entity.getEmployeeIds())
                .build();
    }
}