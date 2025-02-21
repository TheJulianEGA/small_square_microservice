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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
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

}
