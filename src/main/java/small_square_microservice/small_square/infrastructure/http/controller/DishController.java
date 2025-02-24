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
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.application.handler.dishhandler.IDishHandler;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@RestController
@RequestMapping("/api/small_square/dish")
@RequiredArgsConstructor
public class DishController {

    private final IDishHandler dishHandler;

    @Operation(
            summary = "Create a new dish",
            description = "Creates a new dish in the system with the provided request information."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authenticated or lacks necessary permissions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_OWNER )
    @PostMapping("/create")
    public ResponseEntity<DishResponse> createDish(@Valid @RequestBody DishRequest dishRequest) {
        DishResponse dishResponse = dishHandler.createDish(dishRequest);
        return new ResponseEntity<>(dishResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update dish description and price",
            description = "Updates only the description and price of a dish identified by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authenticated or lacks necessary permissions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_OWNER )
    @PatchMapping("/update/{id}")
    public ResponseEntity<DishResponse> updateDish(@PathVariable Long id,
                                                   @Valid @RequestBody DishUpdateRequest dishUpdateRequest) {
        DishResponse updatedDish = dishHandler.updateDish(id, dishUpdateRequest);
        return ResponseEntity.ok(updatedDish);
    }

    @Operation(
            summary = "Toggle dish status (activate/deactivate)",
            description = "Activates or deactivates a dish identified by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authenticated or lacks necessary permissions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_OWNER)
    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<DishResponse> toggleDishStatus(@PathVariable Long id) {
        DishResponse updatedDish = dishHandler.toggleDishStatus(id);
        return ResponseEntity.ok(updatedDish);
    }

}
