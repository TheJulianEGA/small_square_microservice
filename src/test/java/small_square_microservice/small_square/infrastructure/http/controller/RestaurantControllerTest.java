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
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantRequest;
import small_square_microservice.small_square.application.dto.restaurantdto.RestaurantResponse;
import small_square_microservice.small_square.application.handler.restauranthandler.IRestaurantHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private IRestaurantHandler restaurantHandler;

    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    private RestaurantResponse restaurantResponse;
    private RestaurantRequest restaurantRequest;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();

        restaurantResponse = new RestaurantResponse();
        restaurantRequest = new RestaurantRequest();

    }

    @Test
    void createRestaurant_ShouldReturnCreated_WhenRequestIsValid() throws Exception {

        restaurantRequest.setName("Test Restaurant");
        restaurantRequest.setAddress("123 Street");
        restaurantRequest.setOwnerId(1L);
        restaurantRequest.setPhone("1234567890");
        restaurantRequest.setLogoUrl("http://test.com/logo.png");
        restaurantRequest.setNit("123456789");

        when(restaurantHandler.registerRestaurant(any(RestaurantRequest.class))).thenReturn(restaurantResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/small_square/restaurant/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(restaurantResponse)));


        verify(restaurantHandler, times(1)).registerRestaurant(any(RestaurantRequest.class));
    }
}