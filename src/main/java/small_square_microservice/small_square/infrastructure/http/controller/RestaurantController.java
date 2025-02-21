package small_square_microservice.small_square.infrastructure.http.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.handler.restauranthandler.IRestaurantHandler;

@RestController
@RequestMapping("/api/small_square/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final IRestaurantHandler restaurantHandler;


    @Operation(
            summary = "Create a new restaurant",
            description = "This endpoint allows users to register a new restaurant by providing the necessary details.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Restaurant created successfully",
                            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request data", content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal server error", content = @Content
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {

        RestaurantResponse restaurantResponse = restaurantHandler.registerRestaurant(restaurantRequest);

        return new ResponseEntity<>(restaurantResponse, HttpStatus.CREATED);
    }

}
