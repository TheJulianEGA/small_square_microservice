package small_square_microservice.small_square.application.mapper.ordermapper;

import small_square_microservice.small_square.application.dto.orderdishdto.SecurityCodeRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.domain.model.Order;

public interface IOrderMapper {

    Order toModel(OrderRequest request);

    OrderResponse toResponse(Order order);

    Order securityCodeToModel(SecurityCodeRequest request);
}
