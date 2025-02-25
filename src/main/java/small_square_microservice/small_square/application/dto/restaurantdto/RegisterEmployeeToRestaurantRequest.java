package small_square_microservice.small_square.application.dto.restaurantdto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import small_square_microservice.small_square.application.util.ApplicationConstants;

import java.util.List;

@Getter
@Setter
public class RegisterEmployeeToRestaurantRequest {

    @NotEmpty(message = ApplicationConstants.EMPLOYEE_NOT_EMPTY)
    private List<Long> employeeIds;

}
