package small_square_microservice.small_square.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.OrderDishKey;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "order_dishes")
public class OrderDishEntity {

    @EmbeddedId
    private OrderDishKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @MapsId("dishId")
    @JoinColumn(name = "dish_id", nullable = false)
    private DishEntity dish;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}
