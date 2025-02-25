package small_square_microservice.small_square.application.dto.restaurantdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class RestaurantResponse {

    private Long id;

    private String name;

    private String address;

    private Long ownerId;

    private String phone;

    private String logoUrl;

    private String nit;

    private List<Long> employeeIds;

}
