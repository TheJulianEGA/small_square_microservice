package small_square_microservice.small_square.domain.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish {

    private Long id;
    private String name;
    private Category category;
    private String description;
    private Double price;
    private Restaurant restaurant;
    private String imageUrl;
    private Boolean isActive;
    private List<OrderDish> orderDishes;

}