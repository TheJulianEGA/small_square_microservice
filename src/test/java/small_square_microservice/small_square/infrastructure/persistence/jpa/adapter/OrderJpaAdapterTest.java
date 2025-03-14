package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import small_square_microservice.small_square.domain.exception.notfound.OrderNotFoundException;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper.IOrderEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IOrderRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.util.List;
import java.util.Optional;

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
        Long restaurantId = 2L;
        when(orderRepository.existsByClientIdAndRestaurantIdAndStatusIn(
                eq(clientId),
                eq(restaurantId),
                anyList())).thenReturn(true);

        boolean result = orderJpaAdapter.hasPendingOrInProgressOrder(clientId,restaurantId);

        assertTrue(result);
        verify(orderRepository, times(1)).existsByClientIdAndRestaurantIdAndStatusIn(
                eq(clientId),
                eq(restaurantId),
                anyList());
    }

    @Test
    void hasPendingOrInProgressOrder_ShouldReturnFalse_WhenOrderDoesNotExist() {
        Long clientId = 1L;
        Long restaurantId = 2L;
        when(orderRepository.existsByClientIdAndRestaurantIdAndStatusIn(
                eq(clientId),
                eq(restaurantId),
                anyList())).thenReturn(false);

        boolean result = orderJpaAdapter.hasPendingOrInProgressOrder(clientId,restaurantId);

        assertFalse(result);
        verify(orderRepository, times(1)).existsByClientIdAndRestaurantIdAndStatusIn(
                eq(clientId),
                eq(restaurantId),
                anyList());
    }

    @Test
    void getOrdersByStatus_ShouldReturnPaginatedOrders_WhenValidRequest() {
        Long restaurantId = 1L;
        String status = "PENDING";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by("orderPendingDate").ascending());

        List<OrderEntity> orderEntities = List.of(orderEntity);
        Page<OrderEntity> orderPage = new PageImpl<>(orderEntities, pageable, orderEntities.size());

        when(orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable)).thenReturn(orderPage);
        when(orderMapper.toModel(any(OrderEntity.class))).thenReturn(order);

        Paginated<Order> result = orderJpaAdapter.getOrdersByStatus(restaurantId, status, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(order.getId(), result.getContent().get(0).getId());
        verify(orderRepository,times(1)).findByRestaurantIdAndStatus(restaurantId, status, pageable);
        verify(orderMapper,times(1)).toModel(any(OrderEntity.class));
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenOrderExists() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.toModel(orderEntity)).thenReturn(order);

        Order result = orderJpaAdapter.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).toModel(orderEntity);
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderDoesNotExist() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderJpaAdapter.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, never()).toModel(any());
    }

    @Test
    void isOrderAssignedToEmployee_ShouldReturnTrue_WhenOrderIsAssigned() {
        Long orderId = 1L;
        Long employeeId = 2L;

        when(orderRepository.existsByIdAndChefId(orderId, employeeId)).thenReturn(true);

        boolean result = orderJpaAdapter.isOrderAssignedToEmployee(orderId, employeeId);

        assertTrue(result);
        verify(orderRepository, times(1)).existsByIdAndChefId(orderId, employeeId);
    }

    @Test
    void isOrderAssignedToEmployee_ShouldReturnFalse_WhenOrderIsNotAssigned() {
        Long orderId = 1L;
        Long employeeId = 2L;

        when(orderRepository.existsByIdAndChefId(orderId, employeeId)).thenReturn(false);

        boolean result = orderJpaAdapter.isOrderAssignedToEmployee(orderId, employeeId);

        assertFalse(result);
        verify(orderRepository, times(1)).existsByIdAndChefId(orderId, employeeId);
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder_WhenOrderIsUpdatedSuccessfully() {
        when(orderMapper.toEntity(order)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.toModel(orderEntity)).thenReturn(order);

        Order result = orderJpaAdapter.updateOrder(order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).toEntity(order);
        verify(orderMapper, times(1)).toModel(orderEntity);
    }

    @Test
    void isAnOrderInProcessPending_ShouldReturnTrue_WhenOrderExists() {
        Long clientId = 1L;
        Long restaurantId = 2L;

        when(orderRepository.existsByClientIdAndRestaurantIdAndStatus(
                clientId,
                restaurantId,
                InfrastructureConstants.STATUS_PENDING
        )).thenReturn(true);

        boolean result = orderJpaAdapter.isAnOrderInProcessPending(clientId, restaurantId);

        assertTrue(result);
        verify(orderRepository, times(1)).existsByClientIdAndRestaurantIdAndStatus(
                clientId,
                restaurantId,
                InfrastructureConstants.STATUS_PENDING
        );
    }

    @Test
    void isAnOrderInProcessPending_ShouldReturnFalse_WhenOrderDoesNotExist() {
        Long clientId = 1L;
        Long restaurantId = 2L;

        when(orderRepository.existsByClientIdAndRestaurantIdAndStatus(
                clientId,
                restaurantId,
                InfrastructureConstants.STATUS_PENDING
        )).thenReturn(false);

        boolean result = orderJpaAdapter.isAnOrderInProcessPending(clientId, restaurantId);

        assertFalse(result);
        verify(orderRepository, times(1)).existsByClientIdAndRestaurantIdAndStatus(
                clientId,
                restaurantId,
                InfrastructureConstants.STATUS_PENDING
        );
    }
}

