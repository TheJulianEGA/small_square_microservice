package small_square_microservice.small_square.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantEmployee {

    private Long restaurantId;
    private Long employeeId;
}

