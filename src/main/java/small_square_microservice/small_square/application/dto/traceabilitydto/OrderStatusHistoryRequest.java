package small_square_microservice.small_square.application.dto.traceabilitydto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistoryRequest {

    private Long orderId;

    private Long clientId;

    private String customerMail;

    private Long restaurantId;

    private Long employeeId;

    private String employeeMail;

    private String previousState;

    private String newState;

    private LocalDateTime stateChangeTime;

}
