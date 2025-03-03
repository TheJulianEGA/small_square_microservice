package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.model.OrderStatusHistory;


public interface IOrderStatusHistoryFeignPersistencePort {
    void saveOrderStatusHistory (Order order, OrderStatusHistory orderStatusHistory);

}
