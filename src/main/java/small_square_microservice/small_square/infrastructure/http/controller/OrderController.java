package small_square_microservice.small_square.infrastructure.http.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.application.handler.orderhandler.IOrderHandler;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@RestController
@RequestMapping("/api/small_square/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderHandler orderHandler;

    @PreAuthorize(InfrastructureConstants.ROLE_CUSTOMER)
    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse createdOrder = orderHandler.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
}
