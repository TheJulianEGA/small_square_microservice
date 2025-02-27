package small_square_microservice.small_square.application.mapper.messagemapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.application.dto.messagedto.MessageRequest;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.domain.model.MessageModel;

@Component
public class MessageMapper implements IMessageMapper{

    @Override
    public MessageModel toModel (MessageRequest request){
        return MessageModel.builder()
                .message(request.getMessage())
                .build();
    }

    @Override
    public MessageResponse toResponse (MessageModel model){
        return MessageResponse.builder()
                .message(model.getMessage())
                .build();
    }

    @Override
    public MessageModel responseToModel (MessageResponse response){
        return MessageModel.builder()
                .message(response.getMessage())
                .build();
    }

    @Override
    public MessageRequest modelToRequest (MessageModel model){
        return MessageRequest.builder()
                .message(model.getMessage())
                .build();
    }


}
