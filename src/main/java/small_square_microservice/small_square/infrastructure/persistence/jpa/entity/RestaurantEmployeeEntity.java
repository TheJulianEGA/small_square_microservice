package small_square_microservice.small_square.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.RestaurantEmployeeKey;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "restaurant_employees")
public class RestaurantEmployeeEntity {

    @EmbeddedId
    private RestaurantEmployeeKey id;

    @ManyToOne
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

}
