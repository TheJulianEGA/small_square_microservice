package small_square_microservice.small_square.application.mapper.messagemapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import small_square_microservice.small_square.application.dto.messagedto.MessageRequest;
import small_square_microservice.small_square.application.dto.messagedto.MessageResponse;
import small_square_microservice.small_square.domain.model.MessageModel;

import static org.junit.jupiter.api.Assertions.*;

class MessageMapperTest {

    private MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageMapper = new MessageMapper();
    }

    @Test
    void toModel_ShouldReturnMessageModel_WhenGivenMessageRequest() {
        MessageRequest request = MessageRequest.builder()
                .message("Test message")
                .build();

        MessageModel model = messageMapper.toModel(request);

        assertNotNull(model);
        assertEquals("Test message", model.getMessage());
    }

    @Test
    void toResponse_ShouldReturnMessageResponse_WhenGivenMessageModel() {
        MessageModel model = MessageModel.builder()
                .message("Response message")
                .build();

        MessageResponse response = messageMapper.toResponse(model);

        assertNotNull(response);
        assertEquals("Response message", response.getMessage());
    }

    @Test
    void responseToModel_ShouldReturnMessageModel_WhenGivenMessageResponse() {
        MessageResponse response = MessageResponse.builder()
                .message("Mapped response")
                .build();

        MessageModel model = messageMapper.responseToModel(response);

        assertNotNull(model);
        assertEquals("Mapped response", model.getMessage());
    }

    @Test
    void modelToRequest_ShouldReturnMessageRequest_WhenGivenMessageModel() {
        MessageModel model = MessageModel.builder()
                .message("Request from model")
                .build();

        MessageRequest request = messageMapper.modelToRequest(model);

        assertNotNull(request);
        assertEquals("Request from model", request.getMessage());
    }
}