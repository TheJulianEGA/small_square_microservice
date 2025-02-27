package small_square_microservice.small_square.application.mapper.messagemapper;


import small_square_microservice.small_square.application.dto.messagedto.MessageRequest;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.domain.model.MessageModel;

public interface IMessageMapper {

    MessageModel toModel (MessageRequest request);

    MessageResponse toResponse (MessageModel model);

    MessageModel responseToModel (MessageResponse response);

    MessageRequest modelToRequest (MessageModel model);


}
