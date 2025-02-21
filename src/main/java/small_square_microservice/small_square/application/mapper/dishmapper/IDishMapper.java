package small_square_microservice.small_square.application.mapper.dishmapper;

import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.domain.model.Dish;

public interface IDishMapper {

    Dish toModel(DishRequest request);

    DishResponse toResponse(Dish dish);

}
