package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.ordermapper;

import small_square_microservice.small_square.domain.model.Order;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.OrderEntity;

public interface IOrderEntityMapper {

    OrderEntity toEntity(Order order);
    Order toModel(OrderEntity orderEntity);

}
