package small_square_microservice.small_square.application.mapper.ordermapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishResponse;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;

import java.util.List;


@Component
public class OrderMapper implements IOrderMapper {

    @Override
    public Order toModel(OrderRequest request) {
        Order order = new Order();
        order.setRestaurant(
                new Restaurant(request.getRestaurantId(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null));
        List<OrderDish> orderDishes = request.getOrderDishes().stream()
                .map(dishRequest -> new OrderDish(
                        null,
                        new Dish(dishRequest.getDishId(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null),
                        dishRequest.getQuantity()))
                .toList();
        order.setOrderDishes(orderDishes);
        return order;
    }

    @Override
    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setDate(order.getDate());
        response.setStatus(order.getStatus());

        List<OrderDishResponse> orderDishes = order.getOrderDishes().stream()
                .map(dish -> new OrderDishResponse(
                        dish.getDish().getId(),
                        dish.getQuantity()))
                .toList();
        response.setOrderDishes(orderDishes);
        return response;
    }

}
