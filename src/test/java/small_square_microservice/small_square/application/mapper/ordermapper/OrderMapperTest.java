package small_square_microservice.small_square.application.mapper.ordermapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishRequest;
import small_square_microservice.small_square.application.dto.orderdishdto.SecurityCodeRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    void toModel_ShouldMapOrderRequestToOrder() {

        OrderRequest request = OrderRequest.builder()
                .restaurantId(1L)
                .orderDishes(List.of(
                        OrderDishRequest.builder().dishId(101L).quantity(2).build(),
                        OrderDishRequest.builder().dishId(102L).quantity(3).build()
                ))
                .build();

        Order order = orderMapper.toModel(request);

        assertNotNull(order);
        assertEquals(1L, order.getRestaurant().getId());
        assertEquals(2, order.getOrderDishes().size());
        assertEquals(101L, order.getOrderDishes().get(0).getDish().getId());
        assertEquals(2, order.getOrderDishes().get(0).getQuantity());
        assertEquals(102L, order.getOrderDishes().get(1).getDish().getId());
        assertEquals(3, order.getOrderDishes().get(1).getQuantity());
    }

    @Test
    void toResponse_ShouldMapOrderToOrderResponse() {
        // Arrange
        Order order = Order.builder()
                .id(10L)
                .chefId(5L)
                .clientId(20L)
                .date(LocalDateTime.now())
                .status("PENDING")
                .restaurant(Restaurant.builder().id(1L).build())
                .orderDishes(List.of(
                        OrderDish.builder().dish(Dish.builder().id(101L).build()).quantity(2).build(),
                        OrderDish.builder().dish(Dish.builder().id(102L).build()).quantity(3).build()
                ))
                .build();

        // Act
        OrderResponse response = orderMapper.toResponse(order);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(5L, response.getChefId());
        assertEquals(20L, response.getClientId());
        assertEquals("PENDING", response.getStatus());
        assertEquals(1L, response.getRestaurantId());
        assertEquals(2, response.getOrderDishes().size());
        assertEquals(101L, response.getOrderDishes().get(0).getDishId());
        assertEquals(2, response.getOrderDishes().get(0).getQuantity());
        assertEquals(102L, response.getOrderDishes().get(1).getDishId());
        assertEquals(3, response.getOrderDishes().get(1).getQuantity());
    }

    @Test
    void securityCodeToModel_ShouldMapSecurityCodeRequestToOrder() {

        SecurityCodeRequest request = SecurityCodeRequest.builder()
                .securityCode(1234567)
                .build();

        Order order = orderMapper.securityCodeToModel(request);

        assertNotNull(order);
        assertEquals(1234567, order.getSecurityCode());
    }
}