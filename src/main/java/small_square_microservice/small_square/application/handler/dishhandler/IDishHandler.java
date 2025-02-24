package small_square_microservice.small_square.application.handler.dishhandler;

import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;

public interface IDishHandler {

    DishResponse createDish(DishRequest dishRequest);

    DishResponse updateDish(Long id, DishUpdateRequest dishUpdateRequest);

    DishResponse toggleDishStatus(Long id);
}
