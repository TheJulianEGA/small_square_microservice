package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import small_square_microservice.small_square.domain.exception.DishNotFundException;
import small_square_microservice.small_square.domain.model.Dish;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.dishmapper.IDishEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IDishRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        MockitoAnnotations.openMocks(this);

        dish = new Dish();

        dishEntity = new DishEntity();

    }

    @Test
    void createDish_ShouldReturnDish_WhenSuccessfullySaved() {

        dish.setId(1L);
        dish.setName("Pasta");
        dish.setActive(true);

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
        assertTrue(result.getActive());

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
        dish.setActive(true);

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
        assertTrue(result.getActive());

        verify(dishEntityMapper).toEntity(dish);
        verify(dishRepository).save(dishEntity);
        verify(dishEntityMapper).toModel(dishEntity);
    }
}
