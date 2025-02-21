package small_square_microservice.small_square.infrastructure.http.controller;

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

    @PostMapping("/create")
    public ResponseEntity<DishResponse> createDish(@Valid @RequestBody DishRequest dishRequest) {
        DishResponse dishResponse = dishHandler.createDish(dishRequest);
        return new ResponseEntity<>(dishResponse, HttpStatus.CREATED);
    }

}
