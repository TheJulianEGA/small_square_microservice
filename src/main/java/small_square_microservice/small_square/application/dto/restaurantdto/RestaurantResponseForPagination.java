package small_square_microservice.small_square.application.dto.restaurantdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RestaurantResponseForPagination {

    private String name;

    private String logoUrl;
}
