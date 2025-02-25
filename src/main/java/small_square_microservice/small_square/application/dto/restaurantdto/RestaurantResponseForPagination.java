package small_square_microservice.small_square.application.dto.restaurantdto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponseForPagination {

    private String name;

    private String logoUrl;
}
