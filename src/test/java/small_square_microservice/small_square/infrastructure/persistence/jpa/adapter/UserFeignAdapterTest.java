package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import small_square_microservice.small_square.infrastructure.http.feign.IUserFeignClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserFeignAdapterTest {

    @Mock
    private IUserFeignClient userFeignClient;

    @InjectMocks
    private UserFeignAdapter userFeignAdapter;

    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void existsUserWithOwnerRole_ShouldReturnTrue_WhenUserExists() {

        when(userFeignClient.existsUserWithOwnerRole(userId)).thenReturn(true);

        boolean result = userFeignAdapter.existsUserWithOwnerRole(userId);

        assertTrue(result);
        verify(userFeignClient,times(1)).existsUserWithOwnerRole(userId);
    }

    @Test
    void existsUserWithOwnerRole_ShouldReturnFalse_WhenUserDoesNotExist() {
        when(userFeignClient.existsUserWithOwnerRole(userId)).thenThrow(FeignException.NotFound.class);

        boolean result = userFeignAdapter.existsUserWithOwnerRole(userId);

        assertFalse(result);
        verify(userFeignClient,times(1)).existsUserWithOwnerRole(userId);
    }
}
