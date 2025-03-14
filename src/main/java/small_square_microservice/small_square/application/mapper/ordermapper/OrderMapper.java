package small_square_microservice.small_square.application.mapper.ordermapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishResponse;
import small_square_microservice.small_square.application.dto.orderdishdto.SecurityCodeRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderDish;
import small_square_microservice.small_square.domain.model.Restaurant;


@Component
public class OrderMapper implements IOrderMapper {

    @Override
    public Order toModel(OrderRequest request) {
        return Order.builder()
                .restaurant(Restaurant.builder().id(request.getRestaurantId()).build())
                .orderDishes(request.getOrderDishes().stream()
                        .map(dishRequest -> OrderDish.builder()
                                .dish(Dish.builder().id(dishRequest.getDishId()).build())
                                .quantity(dishRequest.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .chefId(order.getChefId())
                .clientId(order.getClientId())
                .date(order.getDate())
                .status(order.getStatus())
                .restaurantId(order.getRestaurant().getId())
                .orderDishes(order.getOrderDishes().stream()
                        .map(dish -> OrderDishResponse.builder()
                                .dishId(dish.getDish().getId())
                                .quantity(dish.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public Order securityCodeToModel(SecurityCodeRequest request){
        return Order.builder()
                .securityCode(request.getSecurityCode())
                .build();
    }
}