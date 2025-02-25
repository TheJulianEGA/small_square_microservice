package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.exception.DishNotFundException;
import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.domain.spi.IOrderPersistencePort;
import small_square_microservice.small_square.domain.usecase.OrderUseCase;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.DishEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper.IOrderEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IDishRepository;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.IOrderRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderMapper;
    private final IDishRepository dishRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderJpaAdapter.class);

    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = orderMapper.toEntity(order);

        return orderMapper.toModel(orderRepository.save(orderEntity));
    }

    @Override
    public boolean hasPendingOrInProgressOrder(Long clientId) {
        return orderRepository.existsByClientIdAndStatusIn(clientId, List.of("Pendiente", "En Preparación", "Listo"));
    }
}
