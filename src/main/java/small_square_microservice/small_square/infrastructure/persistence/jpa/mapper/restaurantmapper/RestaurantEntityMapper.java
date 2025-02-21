package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;

@Component
public class RestaurantEntityMapper implements IRestaurantEntityMapper{

    @Override
    public RestaurantEntity toEntity(Restaurant restaurant) {
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(restaurant.getId());
        entity.setName(restaurant.getName());
        entity.setAddress(restaurant.getAddress());
        entity.setOwnerId(restaurant.getOwnerId());
        entity.setPhone(restaurant.getPhone());
        entity.setLogoUrl(restaurant.getLogoUrl());
        entity.setNit(restaurant.getNit());
        return entity;
    }

    @Override
    public Restaurant toModel(RestaurantEntity entity) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(entity.getId());
        restaurant.setName(entity.getName());
        restaurant.setAddress(entity.getAddress());
        restaurant.setOwnerId(entity.getOwnerId());
        restaurant.setPhone(entity.getPhone());
        restaurant.setLogoUrl(entity.getLogoUrl());
        restaurant.setNit(entity.getNit());
        return restaurant;
    }
}
