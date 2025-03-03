package small_square_microservice.small_square.application.mapper.orderstatushistorymapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.traceabilitydto.OrderStatusHistoryRequest;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderStatusHistory;

@Component
public class OrderStatusHistoryMapper implements IOrderStatusHistoryMapper {

    @Override
    public OrderStatusHistory toModel(OrderStatusHistoryRequest request) {
        return OrderStatusHistory.builder()
                .orderId(request.getOrderId())
                .clientId(request.getClientId())
                .customerMail(request.getCustomerMail())
                .restaurantId(request.getRestaurantId())
                .employeeId(request.getEmployeeId())
                .employeeMail(request.getEmployeeMail())
                .previousState(request.getPreviousState())
                .newState(request.getNewState())
                .stateChangeTime(request.getStateChangeTime())
                .build();
    }

    @Override
    public OrderStatusHistory toOrderStatusHistory(Order model) {
        return OrderStatusHistory.builder()
                .orderId(model.getId())
                .clientId(model.getClientId())
                .restaurantId(model.getRestaurant().getId())
                .employeeId(model.getChefId())
                .newState(model.getStatus())
                .stateChangeTime(model.getDate())
                .build();
    }
    @Override
    public OrderStatusHistoryRequest toRequest(OrderStatusHistory model) {
        return OrderStatusHistoryRequest.builder()
                .orderId(model.getOrderId())
                .clientId(model.getClientId())
                .customerMail(model.getCustomerMail())
                .restaurantId(model.getRestaurantId())
                .employeeId(model.getEmployeeId())
                .employeeMail(model.getEmployeeMail())
                .previousState(model.getPreviousState())
                .newState(model.getNewState())
                .stateChangeTime(model.getStateChangeTime())
                .build();
    }

}
