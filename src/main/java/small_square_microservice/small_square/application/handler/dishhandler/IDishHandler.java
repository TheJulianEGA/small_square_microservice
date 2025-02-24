package small_square_microservice.small_square.application.handler.dishhandler;

import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IDishHandler {

    DishResponse createDish(DishRequest dishRequest);

    DishResponse updateDish(Long id, DishUpdateRequest dishUpdateRequest);

    DishResponse toggleDishStatus(Long id);

    Paginated<DishResponse> getDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId);
}
