package small_square_microservice.small_square.infrastructure.http.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

import java.util.Objects;

public class JwtRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String jwt = request.getHeader(InfrastructureConstants.AUTH_HEADER);
        requestTemplate.header(InfrastructureConstants.AUTH_HEADER,  jwt);
    }
}
