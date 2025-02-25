package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.model.RestaurantEmployee;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEmployeeEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.compositeprimarykey.RestaurantEmployeeKey;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RestaurantEntityMapper implements IRestaurantEntityMapper {

    @Override
    public RestaurantEntity toEntity(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .ownerId(restaurant.getOwnerId())
                .phone(restaurant.getPhone())
                .logoUrl(restaurant.getLogoUrl())
                .nit(restaurant.getNit())
                .build();

        List<RestaurantEmployee> employees = Optional.ofNullable(restaurant.getEmployeeIds())
                .orElse(Collections.emptyList());

        restaurantEntity.setEmployees(employees.stream()
                .map(emp -> RestaurantEmployeeEntity.builder()
                        .id(new RestaurantEmployeeKey(emp.getRestaurantId(), emp.getEmployeeId()))
                        .restaurant(restaurantEntity)
                        .build())
                .collect(Collectors.toList()));

        return restaurantEntity;
    }

    @Override
    public Restaurant toModel(RestaurantEntity entity) {
        return Restaurant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .ownerId(entity.getOwnerId())
                .phone(entity.getPhone())
                .logoUrl(entity.getLogoUrl())
                .nit(entity.getNit())
                .employeeIds(entity.getEmployees()
                        .stream()
                        .map(employeeEntity -> new RestaurantEmployee(
                                employeeEntity.getId().getRestaurantId(),
                                employeeEntity.getId().getEmployeeId()
                        ))
                        .collect(Collectors.toList()))
                .build();
    }
}