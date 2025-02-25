package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;


@Component
public class DishEntityMapper implements IDishEntityMapper {

    @Override
    public Dish toModel(DishEntity entity) {
        return Dish.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(Category.builder()
                        .id(entity.getCategory().getId())
                        .name(entity.getCategory().getName())
                        .description(entity.getCategory().getDescription())
                        .build())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .restaurant(Restaurant.builder()
                        .id(entity.getRestaurant().getId())
                        .build())
                .imageUrl(entity.getImageUrl())
                .isActive(entity.getIsActive())
                .build();
    }

    @Override
    public DishEntity toEntity(Dish model) {
        return DishEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .category(CategoryEntity.builder()
                        .id(model.getCategory().getId())
                        .build())
                .description(model.getDescription())
                .price(model.getPrice())
                .restaurant(RestaurantEntity.builder()
                        .id(model.getRestaurant().getId())
                        .build())
                .imageUrl(model.getImageUrl())
                .isActive(model.getIsActive())
                .build();
    }
}
