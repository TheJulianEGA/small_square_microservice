package small_square_microservice.small_square.infrastructure.http.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import small_square_microservice.small_square.application.dto.messagedto.MessageRequest;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@FeignClient(name = InfrastructureConstants.MESSAGE_SERVICE_NAME, url = InfrastructureConstants.MESSAGE_SERVICE_URL)
public interface IMessageFeignClient {

    @PostMapping("/api/whatsapp/send")
    MessageResponse sendWhatsAppMessage (@RequestBody MessageRequest request);

}
