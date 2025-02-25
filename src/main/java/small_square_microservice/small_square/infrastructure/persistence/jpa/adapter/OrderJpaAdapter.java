package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.spi.IOrderPersistencePort;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper.IOrderEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IOrderRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderMapper;


    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = orderMapper.toEntity(order);

        return orderMapper.toModel(orderRepository.save(orderEntity));
    }

    @Override
    public Paginated<Order> getOrdersByStatus(Long restaurantId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<OrderEntity> orderPage = orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);

        List<Order> orders = orderPage.getContent()
                .stream()
                .map(orderMapper::toModel)
                .toList();

        return new Paginated<>(
                orders,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );
    }

    @Override
    public boolean hasPendingOrInProgressOrder(Long clientId) {
        return orderRepository.existsByClientIdAndStatusIn(clientId,
                List.of(InfrastructureConstants.STATUS_PENDING,
                        InfrastructureConstants.STATUS_IN_PREPARATION,
                        InfrastructureConstants.STATUS_READY ));
    }
}
