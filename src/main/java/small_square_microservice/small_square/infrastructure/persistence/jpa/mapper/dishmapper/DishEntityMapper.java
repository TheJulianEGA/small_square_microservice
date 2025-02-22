package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;

import java.util.ArrayList;

@Component
public class DishEntityMapper implements IDishEntityMapper {

    @Override
     public  Dish toModel(DishEntity entity) {

        Category category = new Category();
        category.setId(entity.getCategory().getId());
        category.setName(entity.getCategory().getName());
        category.setDescription(entity.getCategory().getDescription());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(entity.getRestaurant().getId());

        return new Dish(
                entity.getId(),
                entity.getName(),
                category,
                entity.getDescription(),
                entity.getPrice(),
                restaurant,
                entity.getImageUrl(),
                entity.getIsActive(),
                new ArrayList<>()
        );
    }

    @Override
    public DishEntity toEntity(Dish model) {

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(model.getCategory().getId());

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(model.getRestaurant().getId());

        DishEntity entity = new DishEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setCategory(categoryEntity);
        entity.setDescription(model.getDescription());
        entity.setPrice(model.getPrice());
        entity.setRestaurant(restaurantEntity);
        entity.setImageUrl(model.getImageUrl());
        entity.setIsActive(model.getIsActive());

        return entity;
    }
}
