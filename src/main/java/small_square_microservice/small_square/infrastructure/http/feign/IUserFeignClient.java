package small_square_microservice.small_square.infrastructure.http.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@FeignClient(name = InfrastructureConstants.USER_SERVICE_NAME, url = InfrastructureConstants.USER_SERVICE_URL)
public interface IUserFeignClient {

    @GetMapping("/api/user/exists_user_owner/{userId}")
    boolean existsUserWithOwnerRole(@PathVariable Long userId);

    @GetMapping("/api/user/exists_user_employee/{userId}")
    boolean existsUserWithEmployeeRole(@PathVariable Long userId);

}
