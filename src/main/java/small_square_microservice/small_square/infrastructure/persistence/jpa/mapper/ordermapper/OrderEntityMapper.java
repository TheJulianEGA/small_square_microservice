package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderDishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.OrderDishKey;

import java.util.List;

@Component
public class OrderEntityMapper implements IOrderEntityMapper {

    @Override
    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setClientId(order.getClientId());
        entity.setDate(order.getDate());
        entity.setStatus(order.getStatus());
        entity.setRestaurant(new RestaurantEntity(
                order.getRestaurant().getId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null));
        List<OrderDishEntity> orderDishes = order.getOrderDishes().stream()
                .map(dish -> new OrderDishEntity(new OrderDishKey(
                        order.getId(),
                        dish.getDish().getId()),
                        entity, new DishEntity(
                                dish.getDish().getId(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                        dish.getQuantity()))
                .toList();
        entity.setOrderDishes(orderDishes);
        return entity;
    }

    @Override
    public Order toModel(OrderEntity entity) {
        Order order = new Order();
        order.setId(entity.getId());
        order.setClientId(entity.getClientId());
        order.setDate(entity.getDate());
        order.setStatus(entity.getStatus());
        order.setRestaurant(new Restaurant(
                entity.getRestaurant().getId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null));
        List<OrderDish> orderDishes = entity.getOrderDishes().stream()
                .map(dish -> new OrderDish(
                        order, new Dish(
                                dish.getDish().getId(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                        dish.getQuantity()))
                .toList();
        order.setOrderDishes(orderDishes);
        return order;
    }
}