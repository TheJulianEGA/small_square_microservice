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
public class DishMapper implements IDishMapper {

    @Override
    public Dish toModel(DishRequest request) {
        return Dish.builder()
                .name(request.getName())
                .category(Category.builder().id(request.getCategoryId()).build())
                .description(request.getDescription())
                .price(request.getPrice())
                .restaurant(Restaurant.builder().id(request.getRestaurantId()).build())
                .imageUrl(request.getImageUrl())
                .isActive(true)
                .orderDishes(new ArrayList<>())
                .build();
    }

    @Override
    public DishResponse toResponse(Dish dish) {
        return DishResponse.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .imageUrl(dish.getImageUrl())
                .isActive(dish.getIsActive())
                .categoryId(dish.getCategory().getId())
                .restaurantId(dish.getRestaurant().getId())
                .build();
    }

    @Override
    public Dish updatedDishRequestToModel(DishUpdateRequest dishUpdateRequest) {
        return Dish.builder()
                .description(dishUpdateRequest.getDescription())
                .price(dishUpdateRequest.getPrice())
                .isActive(true)
                .orderDishes(new ArrayList<>())
                .build();
    }
}
