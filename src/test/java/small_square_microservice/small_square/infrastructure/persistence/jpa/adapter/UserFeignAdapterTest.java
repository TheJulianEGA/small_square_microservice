package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.infrastructure.http.feign.IUserFeignClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFeignAdapterTest {

    @Mock
    private IUserFeignClient userFeignClient;

    @InjectMocks
    private UserFeignAdapter userFeignAdapter;

    private final Long userId = 1L;


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
