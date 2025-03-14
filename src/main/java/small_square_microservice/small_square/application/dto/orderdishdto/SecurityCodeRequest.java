package small_square_microservice.small_square.application.dto.orderdishdto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import small_square_microservice.small_square.application.util.ApplicationConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityCodeRequest {

    @NotNull(message = ApplicationConstants.SECURITY_CODE_NOT_NULL)
    private Integer securityCode;

}
