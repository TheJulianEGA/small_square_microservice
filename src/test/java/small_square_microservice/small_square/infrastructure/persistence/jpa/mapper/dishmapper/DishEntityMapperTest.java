package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DishEntityMapperTest {

    @InjectMocks
    private DishEntityMapper dishEntityMapper;


    @Test
    void toModel_ShouldReturnDish_WhenGivenDishEntity() {
        DishEntity entity = DishEntity.builder()
                .id(1L)
                .name("Pasta")
                .category(CategoryEntity.builder()
                        .id(10L)
                        .name("Italian")
                        .description("Italian food")
                        .build())
                .description("Delicious pasta")
                .price(15.99)
                .restaurant(RestaurantEntity.builder().id(5L).build())
                .imageUrl("http://image.com/pasta.jpg")
                .isActive(true)
                .build();

        Dish model = dishEntityMapper.toModel(entity);

        assertNotNull(model);
        assertEquals(1L, model.getId());
        assertEquals("Pasta", model.getName());
        assertEquals(10L, model.getCategory().getId());
        assertEquals("Italian", model.getCategory().getName());
        assertEquals("Italian food", model.getCategory().getDescription());
        assertEquals("Delicious pasta", model.getDescription());
        assertEquals(15.99, model.getPrice());
        assertEquals(5L, model.getRestaurant().getId());
        assertEquals("http://image.com/pasta.jpg", model.getImageUrl());
        assertTrue(model.getIsActive());
    }

    @Test
    void toEntity_ShouldReturnDishEntity_WhenGivenDish() {
        Dish model = Dish.builder()
                .id(1L)
                .name("Pasta")
                .category(Category.builder().id(10L).build())
                .description("Delicious pasta")
                .price(15.99)
                .restaurant(Restaurant.builder().id(5L).build())
                .imageUrl("http://image.com/pasta.jpg")
                .isActive(true)
                .build();

        DishEntity entity = dishEntityMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Pasta", entity.getName());
        assertEquals(10L, entity.getCategory().getId());
        assertEquals("Delicious pasta", entity.getDescription());
        assertEquals(15.99, entity.getPrice());
        assertEquals(5L, entity.getRestaurant().getId());
        assertEquals("http://image.com/pasta.jpg", entity.getImageUrl());
        assertTrue(entity.getIsActive());
    }
}