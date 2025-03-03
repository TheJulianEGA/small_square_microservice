package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderDishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.OrderDishKey;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEntityMapperTest {

    @InjectMocks
    private OrderEntityMapper orderEntityMapper;

    @Mock
    private EntityManager entityManager;


    @Test
    void toEntity_ShouldReturnOrderEntity_WhenGivenOrder() {
        Order order = Order.builder()
                .id(1L)
                .clientId(2L)
                .chefId(3L)
                .date(LocalDateTime.now())
                .securityCode(1234)
                .status("IN_PROGRESS")
                .restaurant(Restaurant.builder().id(5L).build())
                .orderDishes(List.of(
                        OrderDish.builder()
                                .dish(Dish.builder().id(10L).build())
                                .quantity(2)
                                .build()
                ))
                .build();

        DishEntity dishEntityMock = DishEntity.builder().id(10L).build();
        when(entityManager.getReference(DishEntity.class, 10L)).thenReturn(dishEntityMock);

        OrderEntity entity = orderEntityMapper.toEntity(order);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(2L, entity.getClientId());
        assertEquals(3L, entity.getChefId());
        assertEquals(1234, entity.getSecurityCode());
        assertEquals("IN_PROGRESS", entity.getStatus());
        assertEquals(5L, entity.getRestaurant().getId());
        assertEquals(1, entity.getOrderDishes().size());
        assertEquals(10L, entity.getOrderDishes().get(0).getDish().getId());

        verify(entityManager, times(1)).getReference(DishEntity.class, 10L);
    }

    @Test
    void toModel_ShouldReturnOrder_WhenGivenOrderEntity() {
        OrderEntity entity = OrderEntity.builder()
                .id(1L)
                .clientId(2L)
                .chefId(3L)
                .date(LocalDateTime.now())
                .securityCode(1234)
                .status("IN_PROGRESS")
                .restaurant(RestaurantEntity.builder().id(5L).build())
                .orderDishes(List.of(
                        new OrderDishEntity(
                                new OrderDishKey(1L, 10L),
                                null,
                                DishEntity.builder().id(10L).build(),
                                2
                        )
                ))
                .build();

        Order order = orderEntityMapper.toModel(entity);

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(2L, order.getClientId());
        assertEquals(3L, order.getChefId());
        assertEquals(1234, order.getSecurityCode());
        assertEquals("IN_PROGRESS", order.getStatus());
        assertEquals(5L, order.getRestaurant().getId());
        assertEquals(1, order.getOrderDishes().size());
        assertEquals(10L, order.getOrderDishes().get(0).getDish().getId());
    }
}