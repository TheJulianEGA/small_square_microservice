package small_square_microservice.small_square.application.handler.orderhandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.application.mapper.ordermapper.OrderMapper;
import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.model.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @InjectMocks
    private OrderHandler orderHandler;

    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private OrderMapper orderMapper;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private Order order;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setRestaurantId(1L);
        orderRequest.setOrderDishes(List.of(new OrderDishRequest(1L, 2)));

        order = new Order();
        order.setId(1L);

        orderResponse = new OrderResponse();
        orderResponse.setId(1L);
    }

    @Test
    void createOrder_ShouldReturnOrderResponse_WhenOrderIsCreatedSuccessfully() {
        when(orderMapper.toModel(orderRequest)).thenReturn(order);
        when(orderServicePort.createOrder(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderHandler.createOrder(orderRequest);

        assertNotNull(result);
        assertEquals(orderResponse.getId(), result.getId());
        verify(orderMapper, times(1)).toModel(orderRequest);
        verify(orderServicePort, times(1)).createOrder(order);
        verify(orderMapper, times(1)).toResponse(order);
    }
}