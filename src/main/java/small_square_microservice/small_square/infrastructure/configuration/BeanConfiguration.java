package small_square_microservice.small_square.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import small_square_microservice.small_square.domain.api.IDishServicePort;
import small_square_microservice.small_square.domain.api.IRestaurantServicePort;
import small_square_microservice.small_square.domain.spi.ICategoryPersistencePort;
import small_square_microservice.small_square.domain.spi.IDishPersistencePort;
import small_square_microservice.small_square.domain.spi.IRestaurantPersistencePort;
import small_square_microservice.small_square.domain.spi.IUserFeignPersistencePort;
import small_square_microservice.small_square.domain.usecase.DishUseCase;
import small_square_microservice.small_square.domain.usecase.RestaurantUseCase;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public IRestaurantServicePort restaurantServicePort (
            IRestaurantPersistencePort restaurantPersistencePort,
            IUserFeignPersistencePort userFeignPersistencePort
    ) {
        return new RestaurantUseCase(restaurantPersistencePort, userFeignPersistencePort);
    }

    @Bean
    public IDishServicePort dishServicePort (
            IRestaurantPersistencePort restaurantPersistencePort,
            IDishPersistencePort dishPersistencePort,
            ICategoryPersistencePort categoryPersistencePort
    ) {
        return new DishUseCase(dishPersistencePort,restaurantPersistencePort,categoryPersistencePort);
    }


}
