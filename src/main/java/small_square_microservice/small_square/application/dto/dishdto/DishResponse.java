package small_square_microservice.small_square.application.dto.dishdto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Boolean isActive;
    private Long categoryId;
    private Long restaurantId;
}