package small_square_microservice.small_square.application.dto.orderdto;

import lombok.*;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long clientId;
    private Long chefId;
    private LocalDateTime date;
    private String status;
    private Long restaurantId;
    private List<OrderDishResponse> orderDishes;
}
