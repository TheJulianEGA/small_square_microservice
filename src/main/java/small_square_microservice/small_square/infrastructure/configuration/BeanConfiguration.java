package small_square_microservice.small_square.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.api.IOrderServicePort;
import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.security.IAuthenticationSecurityPort;
import small_square_microservice.small_square.domain.spi.*;
import small_square_microservice.small_square.domain.usecase.DishUseCase;
import small_square_microservice.small_square.domain.usecase.OrderUseCase;
import small_square_microservice.small_square.domain.usecase.RestaurantUseCase;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public IRestaurantServicePort restaurantServicePort (
            IRestaurantPersistencePort restaurantPersistencePort,
            IUserFeignPersistencePort userFeignPersistencePort,
            IAuthenticationSecurityPort authenticationSecurityPort
    ) {
        return new RestaurantUseCase(
                restaurantPersistencePort,
                userFeignPersistencePort,
                authenticationSecurityPort);
    }

    @Bean
    public IDishServicePort dishServicePort (
            IRestaurantPersistencePort restaurantPersistencePort,
            IDishPersistencePort dishPersistencePort,
            ICategoryPersistencePort categoryPersistencePort,
            IAuthenticationSecurityPort authenticationSecurityPort
    ) {
        return new DishUseCase(
                dishPersistencePort,
                restaurantPersistencePort,
                categoryPersistencePort,
                authenticationSecurityPort
        );
    }

    @Bean
    public IOrderServicePort orderServicePort(
            IOrderPersistencePort orderPersistencePort,
            IRestaurantPersistencePort restaurantPersistencePort,
            IAuthenticationSecurityPort authenticationSecurityPort
           ) {
        return new OrderUseCase(orderPersistencePort,
                restaurantPersistencePort,
                authenticationSecurityPort
        );
    }


}
