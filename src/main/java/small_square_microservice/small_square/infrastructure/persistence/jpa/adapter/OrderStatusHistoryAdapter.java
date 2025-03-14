package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.application.dto.traceabilitydto.OrderStatusHistoryRequest;
import small_square_microservice.small_square.application.mapper.orderstatushistorymapper.IOrderStatusHistoryMapper;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderStatusHistory;
import small_square_microservice.small_square.domain.spi.IOrderStatusHistoryFeignPersistencePort;
import small_square_microservice.small_square.infrastructure.http.feign.ITraceabilityFeignClient;

@RequiredArgsConstructor
@Service
public class OrderStatusHistoryAdapter implements IOrderStatusHistoryFeignPersistencePort {

    private final ITraceabilityFeignClient traceabilityFeignClient;
    private final IOrderStatusHistoryMapper orderStatusHistoryMapper;

    @Override
    public void saveOrderStatusHistory (Order order,OrderStatusHistory orderStatusHistory){

        OrderStatusHistory history = orderStatusHistoryMapper.toOrderStatusHistory(order);
        history.setCustomerMail(orderStatusHistory.getCustomerMail());
        history.setPreviousState(orderStatusHistory.getPreviousState());
        history.setEmployeeMail(orderStatusHistory.getEmployeeMail());

        OrderStatusHistoryRequest request = orderStatusHistoryMapper.toRequest(history);

        traceabilityFeignClient.saveOrderStatusHistory(request);

    }
}
