package small_square_microservice.small_square.infrastructure.security.filter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import small_square_microservice.small_square.infrastructure.security.service.jwtdetail.JwtDetailsService;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtDetailsService jwtDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final String jwtToken = "valid.jwt.token";
    private final String authHeader = "Bearer " + jwtToken;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {

        UserDetails userDetails = new User("user123", "", List.of());
        when(request.getHeader(InfrastructureConstants.AUTH_HEADER)).thenReturn(authHeader);
        when(jwtDetailsService.loadUserByUsername(jwtToken)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user123", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtDetailsService, times(1)).loadUserByUsername(jwtToken);
    }

    @Test
    void doFilterInternal_NoAuthHeader_DoesNotSetAuthentication() throws ServletException, IOException {

        when(request.getHeader(InfrastructureConstants.AUTH_HEADER)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidAuthHeader_DoesNotSetAuthentication() throws ServletException, IOException {

        when(request.getHeader(InfrastructureConstants.AUTH_HEADER)).thenReturn("InvalidToken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
