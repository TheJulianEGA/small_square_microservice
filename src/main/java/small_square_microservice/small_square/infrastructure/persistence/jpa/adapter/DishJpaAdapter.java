package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;

import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper.IDishEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IDishRepository;

@RequiredArgsConstructor
@Service
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;


    @Override
    public Dish createDish(Dish dish) {

        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        dishEntity.setIsActive(true);

        dishEntity = dishRepository.save(dishEntity);
        return dishEntityMapper.toModel(dishEntity);
    }
}
