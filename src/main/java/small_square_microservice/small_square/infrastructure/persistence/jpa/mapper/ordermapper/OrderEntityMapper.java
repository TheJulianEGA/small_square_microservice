package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.*;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.OrderDishKey;

import java.util.List;

@Component
public class OrderEntityMapper implements IOrderEntityMapper {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrderEntity toEntity(Order order) {
        OrderEntity entity = OrderEntity.builder()
                .id(order.getId())
                .clientId(order.getClientId())
                .chefId(order.getChefId())
                .date(order.getDate())
                .securityCode(order.getSecurityCode())
                .status(order.getStatus())
                .restaurant(RestaurantEntity.builder()
                        .id(order.getRestaurant().getId())
                        .build())
                .build();

        List<OrderDishEntity> orderDishes = order.getOrderDishes().stream()
                .map(orderDish -> new OrderDishEntity(
                        new OrderDishKey(order.getId(), orderDish.getDish().getId()),
                        entity,
                        entityManager.getReference(DishEntity.class, orderDish.getDish().getId()),
                        orderDish.getQuantity()
                ))
                .toList();

        entity.setOrderDishes(orderDishes);
        return entity;
    }

    @Override
    public Order toModel(OrderEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .chefId(entity.getChefId())
                .date(entity.getDate())
                .securityCode(entity.getSecurityCode())
                .status(entity.getStatus())
                .restaurant(Restaurant.builder()
                        .id(entity.getRestaurant().getId())
                        .build())
                .orderDishes(entity.getOrderDishes().stream()
                        .map(dish -> OrderDish.builder()
                                .order(null)
                                .dish(Dish.builder()
                                        .id(dish.getDish().getId())
                                        .build())
                                .quantity(dish.getQuantity())
                                .build())
                        .toList())
                .build();
    }
}
