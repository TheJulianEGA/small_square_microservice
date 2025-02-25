package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEmployeeEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.RestaurantEmployeeKey;

import java.util.Optional;

@Repository
public interface IRestaurantEmployeeRepository extends JpaRepository<RestaurantEmployeeEntity, RestaurantEmployeeKey> {
    Optional<RestaurantEmployeeEntity> findByIdEmployeeId(Long employeeId);
}
