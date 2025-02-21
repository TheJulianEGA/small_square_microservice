package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;

public interface IDishRepository extends JpaRepository<DishEntity,Long> {
}
