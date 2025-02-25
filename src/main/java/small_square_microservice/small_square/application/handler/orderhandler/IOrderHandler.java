package small_square_microservice.small_square.application.handler.orderhandler;

import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;

public interface IOrderHandler {
    OrderResponse createOrder(OrderRequest orderRequest);
}
