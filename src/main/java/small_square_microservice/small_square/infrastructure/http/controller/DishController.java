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
import org.springframework.web.bind.annotation.*;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.application.handler.dishhandler.IDishHandler;

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
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    public ResponseEntity<DishResponse> createDish(@Valid @RequestBody DishRequest dishRequest) {
        DishResponse dishResponse = dishHandler.createDish(dishRequest);
        return new ResponseEntity<>(dishResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update dish description and price",
            description = "Updates only the description and price of a dish identified by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dish updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DishResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Dish not found",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid request body",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PatchMapping("/update/{id}")
    public ResponseEntity<DishResponse> updateDish(@PathVariable Long id,
                                                   @Valid @RequestBody DishUpdateRequest dishUpdateRequest) {
        DishResponse updatedDish = dishHandler.updateDish(id, dishUpdateRequest);
        return ResponseEntity.ok(updatedDish);
    }

}
