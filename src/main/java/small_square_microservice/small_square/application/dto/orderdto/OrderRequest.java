package small_square_microservice.small_square.application.dto.orderdto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import small_square_microservice.small_square.application.dto.orderdishdto.OrderDishRequest;
import small_square_microservice.small_square.application.util.ApplicationConstants;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = ApplicationConstants.RESTAURANT_NOT_NULL)
    private Long restaurantId;

    @NotEmpty(message = ApplicationConstants.ORDER_DISHES_NOT_EMPTY)
    private @Valid List< OrderDishRequest> orderDishes;

}
