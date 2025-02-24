package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity,Long> {
    boolean existsByClientIdAndStatusIn(Long clientId, List<String> pendiente);
}
