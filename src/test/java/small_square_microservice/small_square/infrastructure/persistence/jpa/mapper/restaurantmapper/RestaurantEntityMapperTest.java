package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.model.RestaurantEmployee;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEmployeeEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.RestaurantEmployeeKey;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestaurantEntityMapperTest {
    @InjectMocks
    private RestaurantEntityMapper restaurantEntityMapper;


    @Test
    void toEntity_ShouldReturnRestaurantEntity_WhenGivenRestaurant() {
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Main St")
                .ownerId(100L)
                .phone("123456789")
                .logoUrl("http://logo.com")
                .nit("123456789-0")
                .employeeIds(List.of(new RestaurantEmployee(1L, 200L)))
                .build();

        RestaurantEntity entity = restaurantEntityMapper.toEntity(restaurant);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Test Restaurant", entity.getName());
        assertEquals("123 Main St", entity.getAddress());
        assertEquals(100L, entity.getOwnerId());
        assertEquals("123456789", entity.getPhone());
        assertEquals("http://logo.com", entity.getLogoUrl());
        assertEquals("123456789-0", entity.getNit());
        assertEquals(1, entity.getEmployees().size());
        assertEquals(1L, entity.getEmployees().get(0).getId().getRestaurantId());
        assertEquals(200L, entity.getEmployees().get(0).getId().getEmployeeId());
    }

    @Test
    void toModel_ShouldReturnRestaurant_WhenGivenRestaurantEntity() {
        RestaurantEntity entity = RestaurantEntity.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Main St")
                .ownerId(100L)
                .phone("123456789")
                .logoUrl("http://logo.com")
                .nit("123456789-0")
                .employees(List.of(
                        RestaurantEmployeeEntity.builder()
                                .id(new RestaurantEmployeeKey(1L, 200L))
                                .build()
                ))
                .build();

        Restaurant restaurant = restaurantEntityMapper.toModel(entity);

        assertNotNull(restaurant);
        assertEquals(1L, restaurant.getId());
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals("123 Main St", restaurant.getAddress());
        assertEquals(100L, restaurant.getOwnerId());
        assertEquals("123456789", restaurant.getPhone());
        assertEquals("http://logo.com", restaurant.getLogoUrl());
        assertEquals("123456789-0", restaurant.getNit());
        assertEquals(1, restaurant.getEmployeeIds().size());
        assertEquals(1L, restaurant.getEmployeeIds().get(0).getRestaurantId());
        assertEquals(200L, restaurant.getEmployeeIds().get(0).getEmployeeId());
    }
}