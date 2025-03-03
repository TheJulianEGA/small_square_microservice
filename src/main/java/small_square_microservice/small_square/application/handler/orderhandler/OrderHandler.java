package small_square_microservice.small_square.application.handler.orderhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.application.dto.orderdishdto.SecurityCodeRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.application.mapper.messagemapper.IMessageMapper;
import small_square_microservice.small_square.application.mapper.ordermapper.IOrderMapper;
import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.model.MessageModel;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.util.Paginated;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderMapper orderMapper;
    private final IMessageMapper messageMapper;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        Order order = orderMapper.toModel(orderRequest);
        Order createdOrder = orderServicePort.createOrder(order);
        return orderMapper.toResponse(createdOrder);
    }

    @Override
    public Paginated<OrderResponse> getOrdersByStatus( String status, int page, int size) {
        Paginated<Order> paginatedOrders = orderServicePort.getOrdersByStatus( status, page, size);

        List<OrderResponse> orderResponses = paginatedOrders.getContent()
                .stream()
                .map(orderMapper::toResponse)
                .toList();

        return new Paginated<>(
                orderResponses,
                paginatedOrders.getPageNumber(),
                paginatedOrders.getPageSize(),
                paginatedOrders.getTotalElements(),
                paginatedOrders.getTotalPages()
        );
    }

    @Override
    public OrderResponse assignOrder(Long orderId) {

        Order order = orderServicePort.assignOrder(orderId);

        return orderMapper.toResponse(order);
    }

    @Override
    public MessageResponse orderReady(Long orderId) {

        MessageModel messageModel = orderServicePort.orderReady(orderId);

        return messageMapper.toResponse(messageModel);
    }

    @Override
    public OrderResponse orderDelivery(Long orderId, SecurityCodeRequest securityCodeRequest) {

        Order update = orderServicePort.orderDelivery(orderId, securityCodeRequest.getSecurityCode());

        return orderMapper.toResponse(update);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order update = orderServicePort.cancelOrder(orderId);

        return orderMapper.toResponse(update);
    }
}
