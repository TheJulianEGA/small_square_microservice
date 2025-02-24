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
import small_square_microservice.small_square.application.dto.restaurantdto.RegisterEmployeeToRestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponseForPagination;
import small_square_microservice.small_square.application.handler.restauranthandler.IRestaurantHandler;
import small_square_microservice.small_square.domain.util.Paginated;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@RestController
@RequestMapping("/api/small_square/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final IRestaurantHandler restaurantHandler;


    @Operation(
            summary = "Create a new restaurant",
            description = "Registers a new restaurant with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks necessary permissions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_ADMINISTRATOR )
    @PostMapping("/create")
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest restaurantRequest) {

        RestaurantResponse restaurantResponse = restaurantHandler.registerRestaurant(restaurantRequest);

        return new ResponseEntity<>(restaurantResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "List all restaurants",
            description = "Retrieves a paginated list of restaurants sorted alphabetically."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks necessary permissions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_CUSTOMER)
    @GetMapping("/list")
    public ResponseEntity<Paginated<RestaurantResponseForPagination>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Paginated<RestaurantResponseForPagination> paginatedRestaurants
                = restaurantHandler.getAllRestaurants(page, size);
        return ResponseEntity.ok(paginatedRestaurants);
    }

    @PreAuthorize(InfrastructureConstants.ROLE_OWNER)
    @PutMapping("/update-employees/{restaurantId}")
    public ResponseEntity<RestaurantResponse> updateRestaurantEmployees(
            @PathVariable Long restaurantId,
            @Valid @RequestBody RegisterEmployeeToRestaurantRequest request) {

        RestaurantResponse response = restaurantHandler.updateRestaurantEmployees(restaurantId,request);
        return ResponseEntity.ok(response);
    }

}
