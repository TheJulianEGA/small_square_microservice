package small_square_microservice.small_square.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishRequest;
import small_square_microservice.small_square.application.dto.orderdishdto.SecurityCodeRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.application.handler.orderhandler.IOrderHandler;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private IOrderHandler orderHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private MessageResponse messageResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        orderRequest = new OrderRequest();
        orderResponse = new OrderResponse();
        messageResponse = new MessageResponse();
    }

    @Test
    void createOrder_ShouldReturnCreated_WhenRequestIsValid() throws Exception {
        orderRequest.setRestaurantId(11L);
        orderRequest.setOrderDishes(List.of(
                new OrderDishRequest(17L, 2),
                new OrderDishRequest(18L, 1)
        ));

        when(orderHandler.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/small_square/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderResponse)));

        verify(orderHandler, times(1)).createOrder(any(OrderRequest.class));
    }

    @Test
    void getOrdersByStatus_ShouldReturnOrders_WhenValidRequest() throws Exception {
        String status = "PENDING";
        int page = 0;
        int size = 10;

        List<OrderResponse> orderResponses = List.of(new OrderResponse());

        Paginated<OrderResponse> paginatedOrders = new Paginated<>(orderResponses, page, size, 2L, 1);

        when(orderHandler.getOrdersByStatus(status, page, size)).thenReturn(paginatedOrders);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/small_square/order/get_order_by_status")
                        .param("status", status)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(paginatedOrders)));

        verify(orderHandler, times(1)).getOrdersByStatus(status, page, size);
    }

    @Test
    void assignOrder_ShouldReturnOk_WhenOrderIsAssignedSuccessfully() throws Exception {
        Long orderId = 1L;

        when(orderHandler.assignOrder(orderId)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/small_square/order/assign_employee/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderResponse)));

        verify(orderHandler, times(1)).assignOrder(orderId);
    }

    @Test
    void orderReady_ShouldReturnOk_WhenOrderIsMarkedAsReadySuccessfully() throws Exception {
        Long orderId = 1L;

        when(orderHandler.orderReady(orderId)).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/small_square/order/order_ready/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(messageResponse)));

        verify(orderHandler, times(1)).orderReady(orderId);
    }

    @Test
    void orderDelivery_ShouldReturnOk_WhenOrderIsMarkedAsDeliveredSuccessfully() throws Exception {
        Long orderId = 1L;
        SecurityCodeRequest securityCodeRequest = new SecurityCodeRequest(1234567);

        when(orderHandler.orderDelivery(eq(orderId), any(SecurityCodeRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/small_square/order/order_delivery/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(securityCodeRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderResponse)));

        verify(orderHandler, times(1)).orderDelivery(eq(orderId), any(SecurityCodeRequest.class));
    }

    @Test
    void cancelOrder_ShouldReturnOk_WhenOrderIsCancelledSuccessfully() throws Exception {
        Long orderId = 1L;

        when(orderHandler.cancelOrder(orderId)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/small_square/order/cancel_order/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderResponse)));

        verify(orderHandler, times(1)).cancelOrder(orderId);
    }

}
