package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderDishEntity;

@Repository
public interface IOrderDishRepository extends JpaRepository<OrderDishEntity,Long> {

}
