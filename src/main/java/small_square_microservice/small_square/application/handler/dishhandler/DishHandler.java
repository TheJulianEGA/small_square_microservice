package small_square_microservice.small_square.application.handler.dishhandler;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.mapper.dishmapper.IDishMapper;
import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.model.Dish;

@RequiredArgsConstructor
@Service
public class DishHandler implements IDishHandler{

    private final IDishServicePort dishServicePort;
    private final IDishMapper dishMapper;



    @Override
    public DishResponse createDish(DishRequest dishRequest) {

        Dish dish = dishMapper.toModel(dishRequest);


        Dish createdDish = dishServicePort.createDish(dish);


        return dishMapper.toResponse(createdDish);
    }
}
