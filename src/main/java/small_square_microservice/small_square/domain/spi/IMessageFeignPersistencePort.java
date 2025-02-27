package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.MessageModel;

public interface IMessageFeignPersistencePort {
    MessageModel sendWhatsAppMessage(MessageModel messageModel);
}
