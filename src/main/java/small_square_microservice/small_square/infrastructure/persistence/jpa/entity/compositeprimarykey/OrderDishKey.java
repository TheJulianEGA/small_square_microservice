package small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDishKey implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "dish_id")
    private Long dishId;
}
