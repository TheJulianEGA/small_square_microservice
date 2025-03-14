package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity,Long> {

    boolean existsByClientIdAndRestaurantIdAndStatusIn(Long clientId, Long restaurantId, List<String> statuses);

    Page<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, String status, Pageable pageable);

    boolean existsByIdAndChefId(Long orderId, Long chefId);

    boolean existsByClientIdAndRestaurantIdAndStatus(Long clientId, Long restaurantId, String status);
}
