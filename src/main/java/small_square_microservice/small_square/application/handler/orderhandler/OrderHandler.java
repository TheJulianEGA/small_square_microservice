package small_square_microservice.small_square.application.handler.orderhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.application.mapper.ordermapper.OrderMapper;
import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.model.Order;

@Component
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        Order order = orderMapper.toModel(orderRequest);
        Order createdOrder = orderServicePort.createOrder(order);
        return orderMapper.toResponse(createdOrder);
    }
}
