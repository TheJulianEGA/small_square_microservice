package small_square_microservice.small_square.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDish {

    private Order order;
    private Dish dish;
    private Integer quantity;

}
