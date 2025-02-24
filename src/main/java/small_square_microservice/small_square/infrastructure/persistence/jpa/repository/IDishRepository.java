package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;

public interface IDishRepository extends JpaRepository<DishEntity,Long>, JpaSpecificationExecutor<DishEntity> {
}
