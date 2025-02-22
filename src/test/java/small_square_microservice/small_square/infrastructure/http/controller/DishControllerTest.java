package small_square_microservice.small_square.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import small_square_microservice.small_square.application.dto.dishdto.DishRequest;
import small_square_microservice.small_square.application.dto.dishdto.DishResponse;
import small_square_microservice.small_square.application.dto.dishdto.DishUpdateRequest;
import small_square_microservice.small_square.application.handler.dishhandler.IDishHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    @InjectMocks
    private DishController dishController;

    @Mock
    private IDishHandler dishHandler;

    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    private DishResponse dishResponse;
    private DishRequest dishRequest;
    private DishUpdateRequest dishUpdateRequest;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();

        dishResponse = new DishResponse();
        dishRequest = new DishRequest();
        dishUpdateRequest = new DishUpdateRequest();

    }

    @Test
    void createDish_ShouldReturnCreated_WhenRequestIsValid() throws Exception {

        dishRequest.setName("Test Dish");
        dishRequest.setPrice(1000);
        dishRequest.setRestaurantId(1L);
        dishRequest.setCategoryId(1L);
        dishRequest.setDescription("Test Description");
        dishRequest.setImageUrl("http://test.com/logo.png");

        when(dishHandler.createDish(any(DishRequest.class))).thenReturn(dishResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/small_square/dish/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(dishResponse)));


        verify(dishHandler, times(1)).createDish(any(DishRequest.class));

    }

    @Test
    void updateDish_ShouldReturnUpdatedDish_WhenRequestIsValid() throws Exception {

        dishUpdateRequest.setPrice(1500.0);
        dishUpdateRequest.setDescription("Updated Description");
        Long dishId = 1L;

        when(dishHandler.updateDish(eq(dishId), any(DishUpdateRequest.class)))
                .thenReturn(dishResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/small_square/dish/update/{id}", dishId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(dishResponse)));

        verify(dishHandler, times(1)).updateDish(eq(dishId), any(DishUpdateRequest.class));
    }

    @Test
    void toggleDishStatus_ShouldReturnUpdatedDish_WhenDishExists() throws Exception {
        Long dishId = 1L;

        when(dishHandler.toggleDishStatus((dishId))).thenReturn(dishResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/small_square/dish/toggle-status/{id}", dishId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(dishResponse)));

        verify(dishHandler, times(1)).toggleDishStatus((dishId));
    }
}
