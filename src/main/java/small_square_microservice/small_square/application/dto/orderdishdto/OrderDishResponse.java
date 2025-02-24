package small_square_microservice.small_square.application.dto.orderdishdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishResponse {
    private Long dishId;
    private Integer quantity;
}
