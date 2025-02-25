package small_square_microservice.small_square.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;


public interface IRestaurantRepository extends JpaRepository<RestaurantEntity,Long> {
    //boolean isEmployeeInAnotherRestaurant(Long employeeId, Long currentRestaurantId);
}
