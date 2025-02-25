package small_square_microservice.small_square.domain.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    private Long id;
    private String name;
    private String description;
    private List<Dish> dishes;

   }

