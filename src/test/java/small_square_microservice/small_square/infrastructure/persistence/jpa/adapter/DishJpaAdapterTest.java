package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import small_square_microservice.small_square.domain.exception.DishNotFundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper.IDishEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IDishRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishJpaAdapterTest {

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    @InjectMocks
    private DishJpaAdapter dishJpaAdapter;

    private Dish dish;
    private DishEntity dishEntity;

    @BeforeEach
    void setUp() {

        dish = new Dish();

        dishEntity = new DishEntity();

    }

    @Test
    void createDish_ShouldReturnDish_WhenSuccessfullySaved() {

        dish.setId(1L);
        dish.setName("Pasta");
        dish.setIsActive(true);

        dishEntity.setId(1L);
        dishEntity.setName("Pasta");
        dishEntity.setIsActive(true);

        when(dishEntityMapper.toEntity(dish)).thenReturn(dishEntity);
        when(dishRepository.save(dishEntity)).thenReturn(dishEntity);
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dish);

        Dish result = dishJpaAdapter.createDish(dish);

        assertNotNull(result);
        assertEquals(dish.getId(), result.getId());
        assertEquals(dish.getName(), result.getName());
        assertTrue(result.getIsActive());

        verify(dishEntityMapper).toEntity(dish);
        verify(dishRepository).save(dishEntity);
        verify(dishEntityMapper).toModel(dishEntity);
    }

    @Test
    void getDishById_ShouldReturnDish_WhenDishExists() {
        Long dishId = 1L;
        dishEntity.setId(dishId);
        dishEntity.setName("Pasta");

        dish.setId(dishId);
        dish.setName("Pasta");

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dishEntity));
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dish);

        Dish result = dishJpaAdapter.getDishById(dishId);

        assertNotNull(result);
        assertEquals(dishId, result.getId());
        assertEquals("Pasta", result.getName());

        verify(dishRepository).findById(dishId);
        verify(dishEntityMapper).toModel(dishEntity);
    }

    @Test
    void getDishById_ShouldThrowException_WhenDishDoesNotExist() {
        Long dishId = 1L;

        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        assertThrows(DishNotFundException.class, () -> dishJpaAdapter.getDishById(dishId));

        verify(dishRepository).findById(dishId);
        verifyNoInteractions(dishEntityMapper);
    }

    @Test
    void updateDish_ShouldReturnUpdatedDish_WhenSuccessfullyUpdated() {
        dish.setId(1L);
        dish.setName("Updated Pasta");
        dish.setIsActive(true);

        dishEntity.setId(1L);
        dishEntity.setName("Updated Pasta");
        dishEntity.setIsActive(true);

        when(dishEntityMapper.toEntity(dish)).thenReturn(dishEntity);
        when(dishRepository.save(dishEntity)).thenReturn(dishEntity);
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dish);

        Dish result = dishJpaAdapter.updateDish(dish);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Pasta", result.getName());
        assertTrue(result.getIsActive());

        verify(dishEntityMapper).toEntity(dish);
        verify(dishRepository).save(dishEntity);
        verify(dishEntityMapper).toModel(dishEntity);
    }
    @Test
    void getDishesByRestaurant_ShouldReturnPaginatedDishes_WhenValidRequest() {
        Long restaurantId = 1L;
        Long categoryId = 2L;
        int page = 0;
        int size = 10;

        dish.setName("Pasta");

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        List<DishEntity> dishEntities = List.of(new DishEntity());
        Page<DishEntity> dishPage = new PageImpl<>(dishEntities, pageable, dishEntities.size());

        when(dishRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(dishPage);
        when(dishEntityMapper.toModel(any(DishEntity.class))).thenReturn(dish);

        Paginated<Dish> result = dishJpaAdapter.getDishesByRestaurant(restaurantId, page, size, categoryId);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Pasta", result.getContent().get(0).getName());
        verify(dishRepository).findAll(any(Specification.class), eq(pageable));
    }
}

