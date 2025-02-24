package small_square_microservice.small_square.application.dto.orderdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private LocalDateTime date;
    private String status;
    private Long restaurantId;
    private List<OrderDishResponse> orderDishes;
}
