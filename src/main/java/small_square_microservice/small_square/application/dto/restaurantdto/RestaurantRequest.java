package small_square_microservice.small_square.application.dto.restaurantdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import small_square_microservice.small_square.application.util.ApplicationConstants;

@Setter
@Getter
@NoArgsConstructor
public class RestaurantRequest {

    @NotBlank(message = ApplicationConstants.NAME_NOT_BLANK)
    @Pattern(regexp = "^(?!\\d+$)[a-zA-Z0-9\\s]+$",
            message = ApplicationConstants.NAME_INVALID)

    private String name;

    @NotBlank(message = ApplicationConstants.ADDRESS_NOT_BLANK)
    private String address;

    @NotNull(message = ApplicationConstants.OWNER_ID_NOT_BLANK)
    private Long ownerId;

    @NotBlank(message = ApplicationConstants.PHONE_NOT_BLANK)
    @Pattern(regexp = "^\\+?\\d{1,12}$", message = ApplicationConstants.PHONE_PATTERN)
    private String phone;

    @NotBlank(message = ApplicationConstants.LOGO_URL_NOT_VALID)
    private String logoUrl;

    @NotBlank(message = ApplicationConstants.NIT_NOT_BLANK)
    @Pattern(regexp = "^\\d+$", message = ApplicationConstants.NIT_NUMBER_PATTERN)
    private String nit;

}
