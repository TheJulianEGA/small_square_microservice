package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper;

import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;

public interface IDishEntityMapper {

    Dish toModel(DishEntity entity);

    DishEntity toEntity(Dish model);
}
