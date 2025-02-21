package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper;

import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;

public interface IRestaurantEntityMapper {

    RestaurantEntity toEntity(Restaurant restaurant);

    Restaurant toModel(RestaurantEntity entity);
}
