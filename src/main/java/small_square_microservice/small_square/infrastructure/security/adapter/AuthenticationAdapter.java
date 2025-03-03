package small_square_microservice.small_square.infrastructure.security.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.exception.NoAuthenticatedUserIdFoundException;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.infrastructure.security.service.jwt.JwtService;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@RequiredArgsConstructor
@Service
public class AuthenticationAdapter implements IAuthenticationSecurityPort {

    private final JwtService jwtService;

    @Override
    public Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return parseUserId(userDetails.getUsername());
        } else if (principal instanceof String userId) {
            return parseUserId(userId);
        } else {
            throw new NoAuthenticatedUserIdFoundException(InfrastructureConstants.NO_AUTHENTICATED_USER_ID_FOUND);
        }
    }

    @Override
    public String getAuthenticatedUserEmail() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtService.extractEmail(jwt);
    }

    private Long parseUserId(String userId) {
        try {
            return Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(InfrastructureConstants.ERROR_CONVERTING_USER_ID);
        }
    }
}
