package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.application.dto.messagedto.MessageRequest;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.application.mapper.messagemapper.IMessageMapper;
import small_square_microservice.small_square.domain.model.MessageModel;
import small_square_microservice.small_square.domain.spi.IMessageFeignPersistencePort;
import small_square_microservice.small_square.infrastructure.http.feign.IMessageFeignClient;

@RequiredArgsConstructor
@Service
public class MessageFeignAdapter implements IMessageFeignPersistencePort {

    private final IMessageFeignClient messageFeignClient;
    private final IMessageMapper messageMapper;

    @Override
    public MessageModel sendWhatsAppMessage(MessageModel messageModel) {

        MessageRequest request = messageMapper.modelToRequest(messageModel);

        MessageResponse response = messageFeignClient.sendWhatsAppMessage(request);

        return messageMapper.responseToModel(response);
    }

}
