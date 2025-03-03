package small_square_microservice.small_square.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long id;
    private Long clientId;
    private LocalDateTime date;
    private String status;
    private Long chefId;
    private Restaurant restaurant;
    private Integer securityCode;
    private List<OrderDish> orderDishes;

}