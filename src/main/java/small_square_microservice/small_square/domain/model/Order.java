package small_square_microservice.small_square.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private Integer clientId;
    private LocalDateTime date;
    private String status;
    private Integer chefId;
    private Restaurant restaurant;
    private List<OrderDish> orderDishes;


}