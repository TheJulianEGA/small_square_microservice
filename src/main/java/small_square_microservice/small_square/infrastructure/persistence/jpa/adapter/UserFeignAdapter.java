package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.infrastructure.http.feign.IUserFeignClient;

@RequiredArgsConstructor
@Service
public class UserFeignAdapter implements IUserFeignPersistencePort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean existsUserWithOwnerRole(Long userId) {
        try {
            return userFeignClient.existsUserWithOwnerRole(userId);
        } catch (FeignException.NotFound e) {
            return false;
        }
    }

    @Override
    public boolean existsUserWithEmployeeRole(Long userId) {
        try {
            return userFeignClient.existsUserWithEmployeeRole(userId);
        } catch (FeignException.NotFound e) {
            return false;
        }
    }
}
