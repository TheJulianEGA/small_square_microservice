package small_square_microservice.small_square.infrastructure.persistence.jpa.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "order_pending_date", nullable = false)
    private LocalDateTime orderPendingDate;

    @Column(name = "order_preparation_date")
    private LocalDateTime orderPreparationDate;

    @Column(name = "order_ready_date")
    private LocalDateTime orderReadyDate;

    @Column(name = "order_delivered_date")
    private LocalDateTime orderDeliveredDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "security_code", nullable = true)
    private Integer securityCode;

    @Column(name = "chef_id")
    private Long chefId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDishEntity> orderDishes;

}
