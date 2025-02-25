package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import small_square_microservice.small_square.domain.exception.RestaurantNotFoundException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper.IRestaurantEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IRestaurantRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantJpaAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    @InjectMocks
    private RestaurantJpaAdapter restaurantJpaAdapter;

    private Restaurant restaurant;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {

        restaurant = new Restaurant();
        restaurantEntity = new RestaurantEntity();
    }

    @Test
    void registerRestaurant_ShouldReturnSavedRestaurant() {

        when(restaurantEntityMapper.toEntity(restaurant)).thenReturn(restaurantEntity);
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(restaurantEntityMapper.toModel(restaurantEntity)).thenReturn(restaurant);

        Restaurant savedRestaurant = restaurantJpaAdapter.registerRestaurant(restaurant);

        assertNotNull(savedRestaurant);
        assertEquals(restaurant.getId(), savedRestaurant.getId());
        verify(restaurantRepository).save(restaurantEntity);
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant_WhenFound() {

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toModel(restaurantEntity)).thenReturn(restaurant);

        Restaurant foundRestaurant = restaurantJpaAdapter.getRestaurantById(1L);

        assertNotNull(foundRestaurant);
        assertEquals(restaurant.getId(), foundRestaurant.getId());
        verify(restaurantRepository).findById(1L);
    }

    @Test
    void getRestaurantById_ShouldThrowException_WhenNotFound() {

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                () -> restaurantJpaAdapter.getRestaurantById(1L));
        assertEquals(InfrastructureConstants.RESTAURANT_NOT_FOUND, exception.getMessage());
        verify(restaurantRepository).findById(1L);
    }

    @Test
    void getAllRestaurants_ShouldReturnPaginatedRestaurants() {
        int page = 0;
        int size = 10;

        RestaurantEntity restaurantEntity1 = new RestaurantEntity();
        RestaurantEntity restaurantEntity2 = new RestaurantEntity();
        Restaurant restaurant1 = new Restaurant();
        Restaurant restaurant2 = new Restaurant();

        List<RestaurantEntity> restaurantEntities = List.of(restaurantEntity1, restaurantEntity2);
        List<Restaurant> restaurants = List.of(restaurant1, restaurant2);

        Page<RestaurantEntity> restaurantPage = new PageImpl<>(
                restaurantEntities,
                PageRequest.of(page, size),
                restaurantEntities.size());

        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(restaurantPage);
        when(restaurantEntityMapper.toModel(restaurantEntity1)).thenReturn(restaurant1);
        when(restaurantEntityMapper.toModel(restaurantEntity2)).thenReturn(restaurant2);

        Paginated<Restaurant> result = restaurantJpaAdapter.getAllRestaurants(page, size);

        assertNotNull(result);
        assertEquals(restaurants.size(), result.getContent().size());
        assertEquals(restaurantEntities.size(), result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        verify(restaurantRepository,times(1)).findAll(any(Pageable.class));
        verify(restaurantEntityMapper,times(2)).toModel(any());
    }

    @Test
    void getAllRestaurants_ShouldReturnEmptyPaginated_WhenNoRestaurants() {
        int page = 0;
        int size = 10;
        List<RestaurantEntity> emptyList = List.of();
        Page<RestaurantEntity> emptyPage = new PageImpl<>(emptyList, PageRequest.of(page, size), 0);

        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Paginated<Restaurant> result = restaurantJpaAdapter.getAllRestaurants(page, size);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertTrue(result.getContent().isEmpty());
        verify(restaurantRepository).findAll(any(Pageable.class));
    }

    @Test
    void updateRestaurant_ShouldReturnUpdatedRestaurant() {

        when(restaurantEntityMapper.toEntity(restaurant)).thenReturn(restaurantEntity);
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(restaurantEntityMapper.toModel(restaurantEntity)).thenReturn(restaurant);

        Restaurant updatedRestaurant = restaurantJpaAdapter.updateRestaurant(restaurant);

        assertNotNull(updatedRestaurant);
        assertEquals(restaurant.getId(), updatedRestaurant.getId());
        verify(restaurantEntityMapper).toEntity(restaurant);
        verify(restaurantRepository).save(restaurantEntity);
        verify(restaurantEntityMapper).toModel(restaurantEntity);
    }
}
