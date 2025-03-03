package small_square_microservice.small_square.application.mapper.orderstatushistorymapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import small_square_microservice.small_square.application.dto.traceabilitydto.OrderStatusHistoryRequest;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderStatusHistory;
import small_square_microservice.small_square.domain.model.Restaurant;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusHistoryMapperTest {


    private OrderStatusHistoryMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderStatusHistoryMapper();
    }

    @Test
    void toModel_ShouldMapRequestToModel() {
        OrderStatusHistoryRequest request = OrderStatusHistoryRequest.builder()
                .orderId(1L)
                .clientId(2L)
                .customerMail("customer@example.com")
                .restaurantId(3L)
                .employeeId(4L)
                .employeeMail("employee@example.com")
                .previousState("PENDING")
                .newState("IN_PROGRESS")
                .stateChangeTime(LocalDateTime.now())
                .build();

        OrderStatusHistory model = mapper.toModel(request);

        assertNotNull(model);
        assertEquals(request.getOrderId(), model.getOrderId());
        assertEquals(request.getClientId(), model.getClientId());
        assertEquals(request.getCustomerMail(), model.getCustomerMail());
        assertEquals(request.getRestaurantId(), model.getRestaurantId());
        assertEquals(request.getEmployeeId(), model.getEmployeeId());
        assertEquals(request.getEmployeeMail(), model.getEmployeeMail());
        assertEquals(request.getPreviousState(), model.getPreviousState());
        assertEquals(request.getNewState(), model.getNewState());
        assertEquals(request.getStateChangeTime(), model.getStateChangeTime());
    }

    @Test
    void toOrderStatusHistory_ShouldMapOrderToModel() {
        Order order = new Order();
        order.setId(1L);
        order.setClientId(2L);
        order.setRestaurant(new Restaurant());
        order.setChefId(4L);
        order.setStatus("IN_PROGRESS");
        order.setDate(LocalDateTime.now());

        OrderStatusHistory model = mapper.toOrderStatusHistory(order);

        assertNotNull(model);
        assertEquals(order.getId(), model.getOrderId());
        assertEquals(order.getClientId(), model.getClientId());
        assertEquals(order.getRestaurant().getId(), model.getRestaurantId());
        assertEquals(order.getChefId(), model.getEmployeeId());
        assertEquals(order.getStatus(), model.getNewState());
        assertEquals(order.getDate(), model.getStateChangeTime());
    }

    @Test
    void toRequest_ShouldMapModelToRequest() {
        OrderStatusHistory model = OrderStatusHistory.builder()
                .orderId(1L)
                .clientId(2L)
                .customerMail("customer@example.com")
                .restaurantId(3L)
                .employeeId(4L)
                .employeeMail("employee@example.com")
                .previousState("PENDING")
                .newState("IN_PROGRESS")
                .stateChangeTime(LocalDateTime.now())
                .build();

        OrderStatusHistoryRequest request = mapper.toRequest(model);

        assertNotNull(request);
        assertEquals(model.getOrderId(), request.getOrderId());
        assertEquals(model.getClientId(), request.getClientId());
        assertEquals(model.getCustomerMail(), request.getCustomerMail());
        assertEquals(model.getRestaurantId(), request.getRestaurantId());
        assertEquals(model.getEmployeeId(), request.getEmployeeId());
        assertEquals(model.getEmployeeMail(), request.getEmployeeMail());
        assertEquals(model.getPreviousState(), request.getPreviousState());
        assertEquals(model.getNewState(), request.getNewState());
        assertEquals(model.getStateChangeTime(), request.getStateChangeTime());
    }
}

