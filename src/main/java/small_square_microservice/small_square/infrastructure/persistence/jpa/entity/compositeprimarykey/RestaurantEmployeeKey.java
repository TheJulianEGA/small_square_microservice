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
public class RestaurantEmployeeKey implements Serializable {

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "employee_id")
    private Long employeeId;

}
