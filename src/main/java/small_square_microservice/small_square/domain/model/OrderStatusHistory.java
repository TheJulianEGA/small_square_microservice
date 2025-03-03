package small_square_microservice.small_square.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistory {

    private String id;

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
