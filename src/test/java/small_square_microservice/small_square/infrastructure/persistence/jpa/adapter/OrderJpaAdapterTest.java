package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper.IOrderEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IOrderRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderJpaAdapterTest {

    @InjectMocks
    private OrderJpaAdapter orderJpaAdapter;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderEntityMapper orderMapper;

    private Order order;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);

        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
    }

    @Test
    void createOrder_ShouldReturnOrder_WhenSavedSuccessfully() {
        when(orderMapper.toEntity(order)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.toModel(orderEntity)).thenReturn(order);

        Order result = orderJpaAdapter.createOrder(order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).toEntity(order);
        verify(orderMapper, times(1)).toModel(orderEntity);
    }

    @Test
    void hasPendingOrInProgressOrder_ShouldReturnTrue_WhenOrderExists() {
        Long clientId = 1L;
        when(orderRepository.existsByClientIdAndStatusIn(eq(clientId), anyList())).thenReturn(true);

        boolean result = orderJpaAdapter.hasPendingOrInProgressOrder(clientId);

        assertTrue(result);
        verify(orderRepository, times(1)).existsByClientIdAndStatusIn(eq(clientId), anyList());
    }

    @Test
    void hasPendingOrInProgressOrder_ShouldReturnFalse_WhenOrderDoesNotExist() {
        Long clientId = 1L;
        when(orderRepository.existsByClientIdAndStatusIn(eq(clientId), anyList())).thenReturn(false);

        boolean result = orderJpaAdapter.hasPendingOrInProgressOrder(clientId);

        assertFalse(result);
        verify(orderRepository, times(1)).existsByClientIdAndStatusIn(eq(clientId), anyList());
    }
}