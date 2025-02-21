package small_square_microservice.small_square.application.dto.dishdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import small_square_microservice.small_square.application.util.ApplicationConstants;

@Getter
@Setter
public class DishRequest {

    @NotBlank(message = ApplicationConstants.NAME_NOT_BLANK)
    private String name;

    @NotNull(message = ApplicationConstants.PRICE_NOT_NULL)
    @Min(value = 1, message = ApplicationConstants.PRICE_NOT_VALID)
    private double price;

    @NotBlank(message = ApplicationConstants.DESCRIPTION_NOT_BLANK)
    private String description;

    @NotBlank(message = ApplicationConstants.IMAGE_URL_NOT_VALID)
    private String imageUrl;

    @NotNull(message = ApplicationConstants.CATEGORY_NOT_NULL)
    private Long categoryId;

    @NotNull(message = ApplicationConstants.RESTAURANT_NOT_NULL)
    private Long restaurantId;

}
