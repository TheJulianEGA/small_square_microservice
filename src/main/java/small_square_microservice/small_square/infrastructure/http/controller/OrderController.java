package small_square_microservice.small_square.infrastructure.http.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.application.dto.orderdto.OrderRequest;
import small_square_microservice.small_square.application.dto.orderdto.OrderResponse;
import small_square_microservice.small_square.application.handler.orderhandler.IOrderHandler;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@RestController
@RequestMapping("/api/small_square/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderHandler orderHandler;

    @Operation(
            summary = "Create an order",
            description = "Creates a new order for a restaurant. Only accessible to authenticated customers."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied - only customers can place orders",
                    content = @Content(mediaType = "application/json")),
    })
    @PreAuthorize(InfrastructureConstants.ROLE_CUSTOMER)
    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse createdOrder = orderHandler.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @Operation(
            summary = "Get orders by status",
            description = "Retrieves a paginated list of orders filtered by status. Only accessible to employees."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied - only employees can view orders",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_EMPLOYEE)
    @GetMapping("get_order_by_status")
    public ResponseEntity<Paginated<OrderResponse>> getOrdersByStatus(
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size) {

        Paginated<OrderResponse> orders = orderHandler.getOrdersByStatus( status, page, size);

        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Assign an employee to an order",
            description = "Allows a restaurant employee to assign themselves to an order and update its status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully assigned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied - only employees can assign orders",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_EMPLOYEE)
    @PutMapping("/assign_employee/{orderId}")
    public ResponseEntity<OrderResponse> assignOrder(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderHandler.assignOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @PreAuthorize(InfrastructureConstants.ROLE_EMPLOYEE)
    @PutMapping("/order_ready/{orderId}")
    public ResponseEntity<MessageResponse> orderReady(@PathVariable Long orderId) {
        MessageResponse messageResponse = orderHandler.orderReady(orderId);
        return ResponseEntity.ok(messageResponse);
    }
}
