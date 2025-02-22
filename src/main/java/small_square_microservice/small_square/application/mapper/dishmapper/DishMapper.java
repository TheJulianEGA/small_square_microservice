package small_square_microservice.small_square.application.mapper.dishmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;

import java.util.ArrayList;

@Component
public class DishMapper implements IDishMapper{

    @Override
    public Dish toModel(DishRequest request) {

        Category category = new Category();
        category.setId(request.getCategoryId());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(request.getRestaurantId());

        return new Dish(
                null,
                request.getName(),
                category,
                request.getDescription(),
                request.getPrice(),
                restaurant,
                request.getImageUrl(),
                true,
                new ArrayList<>()
        );
    }

    @Override
    public DishResponse toResponse(Dish dish) {

        return new DishResponse(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getImageUrl(),
                dish.getIsActive(),
                dish.getCategory().getId(),
                dish.getRestaurant().getId()
        );
    }

    @Override
    public Dish updatedDishRequestToModel(DishUpdateRequest dishUpdateRequest) {
        return new Dish(
                null,
                null,
                null,
                dishUpdateRequest.getDescription(),
                dishUpdateRequest.getPrice(),
                null,
                null,
                true,
                new ArrayList<>()
        );
    }

}
