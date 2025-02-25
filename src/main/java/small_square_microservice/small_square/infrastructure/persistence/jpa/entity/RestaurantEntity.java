package small_square_microservice.small_square.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "nit", nullable = false)
    private String nit;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DishEntity> dishes;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEntity> orders;

    @ElementCollection
    @CollectionTable(name = "restaurant_employees", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "employee_id")
    private List<Long> employeeIds;

}
