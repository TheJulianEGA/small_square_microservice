package small_square_microservice.small_square.application.dto.orderdishdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import small_square_microservice.small_square.application.util.ApplicationConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishRequest {

    @NotNull(message = ApplicationConstants.DISH_NOT_NULL)
    private Long dishId;

    @NotNull(message = ApplicationConstants.QUANTITY_NOT_NULL)
    @Min(value = 1, message = ApplicationConstants.QUANTITY_NOT_VALID)
    private Integer quantity;
}
