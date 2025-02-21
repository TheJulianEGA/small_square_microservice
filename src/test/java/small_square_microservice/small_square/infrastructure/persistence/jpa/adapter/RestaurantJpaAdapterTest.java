package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import small_square_microservice.small_square.domain.exception.RestaurantNotFundException;
import small_square_microservice.small_square.domain.model.Restaurant;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.RestaurantEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.restaurantmapper.IRestaurantEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IRestaurantRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        MockitoAnnotations.openMocks(this);

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

        RestaurantNotFundException exception = assertThrows(RestaurantNotFundException.class,
                () -> restaurantJpaAdapter.getRestaurantById(1L));
        assertEquals(InfrastructureConstants.RESTAURANT_NOT_FOUND, exception.getMessage());
        verify(restaurantRepository).findById(1L);
    }
}
