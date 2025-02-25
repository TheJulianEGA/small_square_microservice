package small_square_microservice.small_square.domain.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant {

    private Long id;
    private String name;
    private String address;
    private Long ownerId;
    private String phone;
    private String logoUrl;
    private String nit;
    private List<Dish> dishes;
    private List<Order> orders;
    private List<Long> employeeIds;

}
