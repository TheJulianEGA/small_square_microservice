package small_square_microservice.small_square.application.dto.orderdishdto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDishResponse {
    private Long dishId;
    private Integer quantity;
}
