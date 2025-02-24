package small_square_microservice.small_square.infrastructure.persistence.jpa.specification;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;

import java.util.ArrayList;
import java.util.List;

public class DishSpecification {

    public static Specification<DishEntity> byRestaurantAndCategory(Long restaurantId, Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("restaurant").get("id"), restaurantId));

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private DishSpecification() {}
}