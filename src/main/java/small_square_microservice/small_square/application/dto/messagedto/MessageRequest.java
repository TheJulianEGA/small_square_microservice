package small_square_microservice.small_square.application.dto.messagedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {

    private String message;

}
