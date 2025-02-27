package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.exception.notfound.DishNotFoundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;

import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper.IDishEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IDishRepository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.specification.DishSpecification;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.util.List;

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
                .orElseThrow(() -> new DishNotFoundException(InfrastructureConstants.DISH_NOT_FOUND));
    }

    @Override
    public Dish updateDish(Dish dish) {

        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dish));

        return dishEntityMapper.toModel(dishEntity);
    }

    @Override
    public Paginated<Dish> getDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Specification<DishEntity> specification = DishSpecification.byRestaurantAndCategory(restaurantId, categoryId);
        Page<DishEntity> dishPage = dishRepository.findAll(specification, pageable);

        List<Dish> dishes = dishPage.getContent()
                .stream()
                .map(dishEntityMapper::toModel)
                .toList();

        return new Paginated<>(
                dishes,
                dishPage.getNumber(),
                dishPage.getSize(),
                dishPage.getTotalElements(),
                dishPage.getTotalPages()
        );
    }
}
