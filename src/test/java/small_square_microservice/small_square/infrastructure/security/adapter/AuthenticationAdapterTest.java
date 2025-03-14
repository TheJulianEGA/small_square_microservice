package small_square_microservice.small_square.infrastructure.security.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import small_square_microservice.small_square.domain.exception.NoAuthenticatedUserIdFoundException;
@ExtendWith(MockitoExtension.class)
class AuthenticationAdapterTest {

    @InjectMocks
    private AuthenticationAdapter authenticationAdapter;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getAuthenticatedUserId_ShouldReturnUserId_WhenPrincipalIsUserDetails() {

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("123");

        Long result = authenticationAdapter.getAuthenticatedUserId();

        assertNotNull(result);
        assertEquals(123L, result);
    }

    @Test
    void getAuthenticatedUserId_ShouldReturnUserId_WhenPrincipalIsString() {
        when(authentication.getPrincipal()).thenReturn("456");

        Long result = authenticationAdapter.getAuthenticatedUserId();

        assertNotNull(result);
        assertEquals(456L, result);
    }

    @Test
    void getAuthenticatedUserId_ShouldThrowNumberFormatException_WhenPrincipalIsInvalidString() {
        when(authentication.getPrincipal()).thenReturn("invalid");

        assertThrows(NumberFormatException.class, () -> authenticationAdapter.getAuthenticatedUserId());
    }

    @Test
    void getAuthenticatedUserId_ShouldThrowNoAuthenticatedUserIdFoundException_WhenPrincipalIsInvalidType() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        assertThrows(NoAuthenticatedUserIdFoundException.class, () -> authenticationAdapter.getAuthenticatedUserId());
    }
}