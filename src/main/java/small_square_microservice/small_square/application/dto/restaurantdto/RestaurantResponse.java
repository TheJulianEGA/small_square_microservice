package small_square_microservice.small_square.application.dto.restaurantdto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestaurantResponse {

    private Long id;

    private String name;

    private String address;

    private Long ownerId;

    private String phone;

    private String logoUrl;

    private String nit;

}
