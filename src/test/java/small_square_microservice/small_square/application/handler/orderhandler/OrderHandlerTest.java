package small_square_microservice.small_square.application.handler.orderhandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishRequest;
import small_square_microservice.small_square.application.dto.orderdishdto.SecurityCodeRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.application.mapper.messagemapper.MessageMapper;
import small_square_microservice.small_square.application.mapper.ordermapper.OrderMapper;
import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.model.MessageModel;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.util.Paginated;

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

    @Mock
    private MessageMapper messageMapper;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private Order order;
    private  MessageResponse messageResponse;
    private MessageModel messageModel;


    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setRestaurantId(1L);
        orderRequest.setOrderDishes(List.of(new OrderDishRequest(1L, 2)));

        order = new Order();
        order.setId(1L);

        orderResponse = new OrderResponse();
        orderResponse.setId(1L);

        messageResponse = new MessageResponse();
        messageResponse.setMessage("hola");

        messageModel = new MessageModel();
        messageModel.setMessage("hola");

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

    @Test
    void getOrdersByStatus_ShouldReturnPaginatedOrderResponses_WhenValidRequest() {
        String status = "PENDING";
        int page = 0;
        int size = 10;

        Paginated<Order> paginatedOrders = new Paginated<>(
                List.of(order),
                page,
                size,
                1L,
                1
        );

        when(orderServicePort.getOrdersByStatus(status, page, size)).thenReturn(paginatedOrders);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        Paginated<OrderResponse> result = orderHandler.getOrdersByStatus(status, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(orderResponse.getId(), result.getContent().get(0).getId());
        assertEquals(paginatedOrders.getPageNumber(), result.getPageNumber());
        assertEquals(paginatedOrders.getPageSize(), result.getPageSize());
        assertEquals(paginatedOrders.getTotalElements(), result.getTotalElements());
        assertEquals(paginatedOrders.getTotalPages(), result.getTotalPages());

        verify(orderServicePort, times(1)).getOrdersByStatus(status, page, size);
        verify(orderMapper, times(1)).toResponse(order);
    }

    @Test
    void assignOrder_ShouldReturnOrderResponse_WhenOrderIsAssignedSuccessfully() {
        Long orderId = 1L;

        when(orderServicePort.assignOrder(orderId)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderHandler.assignOrder(orderId);

        assertNotNull(result);
        assertEquals(orderResponse.getId(), result.getId());
        verify(orderServicePort, times(1)).assignOrder(orderId);
        verify(orderMapper, times(1)).toResponse(order);
    }

    @Test
    void orderReady_ShouldReturnMessageResponse_WhenOrderIsMarkedAsReadySuccessfully() {
        Long orderId = 1L;

        when(orderServicePort.orderReady(orderId)).thenReturn(messageModel);
        when(messageMapper.toResponse(messageModel)).thenReturn(messageResponse);

        MessageResponse result = orderHandler.orderReady(orderId);

        assertNotNull(result);
        assertEquals(messageResponse.getMessage(), result.getMessage());
        verify(orderServicePort, times(1)).orderReady(orderId);
        verify(messageMapper, times(1)).toResponse(messageModel);
    }
    @Test
    void orderDelivery_ShouldReturnOrderResponse_WhenSecurityCodeSuccessfully() {
        Long orderId = 1L;
        SecurityCodeRequest securityCodeRequest = new SecurityCodeRequest(1234567);

        when(orderServicePort.orderDelivery(orderId, securityCodeRequest.getSecurityCode())).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderHandler.orderDelivery(orderId, securityCodeRequest);

        assertNotNull(result);
        assertEquals(orderResponse.getId(), result.getId());

        verify(orderServicePort, times(1)).orderDelivery(orderId, securityCodeRequest.getSecurityCode());
        verify(orderMapper, times(1)).toResponse(order);
    }

    @Test
    void cancelOrder_ShouldReturnMessageResponse_WhenOrderIsCancelledSuccessfully() {
        Long orderId = 1L;

        when(orderServicePort.cancelOrder(orderId)).thenReturn(messageModel);
        when(messageMapper.toResponse(messageModel)).thenReturn(messageResponse);

        MessageResponse result = orderHandler.cancelOrder(orderId);

        assertNotNull(result);
        assertEquals(messageResponse.getMessage(), result.getMessage());
        verify(orderServicePort, times(1)).cancelOrder(orderId);
        verify(messageMapper, times(1)).toResponse(messageModel);
    }

}