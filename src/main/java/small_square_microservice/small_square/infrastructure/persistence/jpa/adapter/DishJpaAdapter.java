package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.exception.DishNotFundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;

import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper.IDishEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IDishRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

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

    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .map(dishEntityMapper::toModel)
                .orElseThrow(() -> new DishNotFundException(InfrastructureConstants.DISH_NOT_FOUND));
    }

    @Override
    public Dish updateDish(Dish dish) {

        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dish));

        return dishEntityMapper.toModel(dishEntity);
    }
}
