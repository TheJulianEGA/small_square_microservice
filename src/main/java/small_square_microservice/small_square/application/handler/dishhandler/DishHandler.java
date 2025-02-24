package small_square_microservice.small_square.application.handler.dishhandler;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.application.mapper.dishmapper.IDishMapper;
import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

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

    @Override
    public DishResponse updateDish(Long id, DishUpdateRequest dishUpdateRequest) {

        Dish dish = dishMapper.updatedDishRequestToModel(dishUpdateRequest);

        Dish updatedDish = dishServicePort.updatedDishById(id,dish);

        return dishMapper.toResponse(updatedDish);
    }

    @Override
    public DishResponse toggleDishStatus(Long id) {
        Dish updatedDish = dishServicePort.toggleDishStatus(id);
        return dishMapper.toResponse(updatedDish);
    }

    @Override
    public Paginated<DishResponse> getDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId) {

        Paginated<Dish> paginatedDishes = dishServicePort.getDishesByRestaurant(restaurantId, page, size, categoryId);

        List<DishResponse> dishResponses = paginatedDishes.getContent()
                .stream()
                .map(dishMapper::toResponse)
                .toList();

        return new Paginated<>(
                dishResponses,
                paginatedDishes.getPageNumber(),
                paginatedDishes.getPageSize(),
                paginatedDishes.getTotalElements(),
                paginatedDishes.getTotalPages()
        );
    }
}
