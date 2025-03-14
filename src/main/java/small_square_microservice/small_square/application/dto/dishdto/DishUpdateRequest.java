package small_square_microservice.small_square.application.dto.dishdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import small_square_microservice.small_square.application.util.ApplicationConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishUpdateRequest {

    @NotNull(message = ApplicationConstants.PRICE_NOT_NULL)
    @Min(value = 1, message = ApplicationConstants.PRICE_NOT_VALID)
    private Double price;

    @NotBlank(message = ApplicationConstants.DESCRIPTION_NOT_BLANK)
    private String description;

}
