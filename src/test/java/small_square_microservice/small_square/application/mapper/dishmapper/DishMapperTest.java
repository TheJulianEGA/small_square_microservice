package small_square_microservice.small_square.application.mapper.dishmapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Restaurant;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DishMapperTest {
    private DishMapper dishMapper;

    @BeforeEach
    void setUp() {
        dishMapper = new DishMapper();
    }

    @Test
    void toModel_ShouldReturnDish_WhenGivenDishRequest() {
        DishRequest request = DishRequest.builder()
                .name("Pasta")
                .categoryId(1L)
                .description("Delicious pasta with sauce")
                .price(12.99)
                .restaurantId(2L)
                .imageUrl("http://example.com/pasta.jpg")
                .build();

        Dish dish = dishMapper.toModel(request);

        assertNotNull(dish);
        assertEquals("Pasta", dish.getName());
        assertEquals(1L, dish.getCategory().getId());
        assertEquals("Delicious pasta with sauce", dish.getDescription());
        assertEquals(12.99, dish.getPrice());
        assertEquals(2L, dish.getRestaurant().getId());
        assertEquals("http://example.com/pasta.jpg", dish.getImageUrl());
        assertTrue(dish.getIsActive());
        assertEquals(new ArrayList<>(), dish.getOrderDishes());
    }

    @Test
    void toResponse_ShouldReturnDishResponse_WhenGivenDish() {
        Dish dish = Dish.builder()
                .id(10L)
                .name("Burger")
                .description("Juicy beef burger")
                .price(12.99)
                .imageUrl("http://example.com/burger.jpg")
                .isActive(true)
                .category(Category.builder().id(3L).build())
                .restaurant(Restaurant.builder().id(4L).build())
                .build();

        DishResponse response = dishMapper.toResponse(dish);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Burger", response.getName());
        assertEquals("Juicy beef burger", response.getDescription());
        assertEquals(12.99, response.getPrice());
        assertEquals("http://example.com/burger.jpg", response.getImageUrl());
        assertTrue(response.getIsActive());
        assertEquals(3L, response.getCategoryId());
        assertEquals(4L, response.getRestaurantId());
    }

    @Test
    void updatedDishRequestToModel_ShouldReturnUpdatedDish_WhenGivenDishUpdateRequest() {
        DishUpdateRequest updateRequest = DishUpdateRequest.builder()
                .description("Updated description")
                .price(12.99)
                .build();

        Dish dish = dishMapper.updatedDishRequestToModel(updateRequest);

        assertNotNull(dish);
        assertEquals("Updated description", dish.getDescription());
        assertEquals(12.99, dish.getPrice());
        assertTrue(dish.getIsActive());
        assertEquals(new ArrayList<>(), dish.getOrderDishes());
    }
}