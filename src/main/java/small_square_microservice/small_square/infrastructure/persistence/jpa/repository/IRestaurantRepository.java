package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;

import java.util.Optional;

@Repository
public interface IRestaurantRepository extends JpaRepository<RestaurantEntity,Long> {

    Optional<RestaurantEntity> findByEmployees_Id_EmployeeId(Long employeeId);
}
