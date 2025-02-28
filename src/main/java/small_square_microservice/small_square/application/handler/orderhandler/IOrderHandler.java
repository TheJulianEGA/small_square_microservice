package small_square_microservice.small_square.application.handler.orderhandler;

import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.application.dto.orderdishdto.SecurityCodeRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.domain.util.Paginated;

public interface IOrderHandler {
    OrderResponse createOrder(OrderRequest orderRequest);

    Paginated<OrderResponse> getOrdersByStatus( String status, int page, int size);

    OrderResponse assignOrder(Long orderId);

    MessageResponse orderReady(Long orderId);

    OrderResponse orderDelivery(Long orderId, SecurityCodeRequest securityCodeRequest);
}
