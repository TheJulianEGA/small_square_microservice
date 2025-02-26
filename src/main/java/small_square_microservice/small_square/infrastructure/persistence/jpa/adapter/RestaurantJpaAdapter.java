package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.exception.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper.IRestaurantEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IRestaurantEmployeeRepository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IRestaurantRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.util.List;


@RequiredArgsConstructor
@Service
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant registerRestaurant(Restaurant restaurant) {

        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);
        restaurantEntity = restaurantRepository.save(restaurantEntity);
        return restaurantEntityMapper.toModel(restaurantEntity);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {

        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::toModel)
                .orElseThrow( () -> new RestaurantNotFoundException(InfrastructureConstants.RESTAURANT_NOT_FOUND));
    }

    @Override
    public Paginated<Restaurant> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<RestaurantEntity> restaurantPage = restaurantRepository.findAll(pageable);

        List<Restaurant> restaurants = restaurantPage.getContent()
                .stream()
                .map(restaurantEntityMapper::toModel)
                .toList();

        return new Paginated<>(
                restaurants,
                restaurantPage.getNumber(),
                restaurantPage.getSize(),
                restaurantPage.getTotalElements(),
                restaurantPage.getTotalPages()
        );
    }

    @Override
    public Restaurant updateRestaurant(Restaurant restaurantToUpdate) {
        RestaurantEntity restaurantEntity = restaurantRepository.save(restaurantEntityMapper
                .toEntity(restaurantToUpdate));

        return restaurantEntityMapper.toModel(restaurantEntity);
    }

    @Override
    public Long findRestaurantByEmployeeId(Long employeeId) {
        return restaurantRepository.findByEmployees_Id_EmployeeId(employeeId)
                .map(RestaurantEntity::getId)
                .orElse(null);
    }

    @Override
    public Long getRestaurantByEmployeeId(Long employeeId) {
        return restaurantEmployeeRepository.findByIdEmployeeId(employeeId)
                .map(employee -> employee.getId().getRestaurantId())
                .orElse(null);
    }

}
