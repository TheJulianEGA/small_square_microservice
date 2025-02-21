package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper.IRestaurantEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IRestaurantRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;


@RequiredArgsConstructor
@Service
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
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
                .orElseThrow( () -> new RestaurantNotFundException(InfrastructureConstants.RESTAURANT_NOT_FOUND));
    }
}
