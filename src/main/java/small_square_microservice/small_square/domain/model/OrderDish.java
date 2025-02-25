package small_square_microservice.small_square.domain.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDish {

    private Order order;
    private Dish dish;
    private Integer quantity;

}
