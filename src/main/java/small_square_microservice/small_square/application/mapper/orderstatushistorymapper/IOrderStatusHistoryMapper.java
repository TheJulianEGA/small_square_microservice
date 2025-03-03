package small_square_microservice.small_square.application.mapper.orderstatushistorymapper;

import small_square_microservice.small_square.application.dto.traceabilitydto.OrderStatusHistoryRequest;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderStatusHistory;

public interface IOrderStatusHistoryMapper {

    OrderStatusHistory toModel(OrderStatusHistoryRequest request);
    OrderStatusHistory toOrderStatusHistory(Order model);
    OrderStatusHistoryRequest toRequest(OrderStatusHistory model);


}
