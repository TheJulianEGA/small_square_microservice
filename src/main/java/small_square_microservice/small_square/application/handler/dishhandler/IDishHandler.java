package small_square_microservice.small_square.application.handler.dishhandler;

import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;

public interface IDishHandler {

    DishResponse createDish(DishRequest dishRequest);

}
