package small_square_microservice.small_square.domain.spi;

public interface IUserFeignPersistencePort {

    boolean existsUserWithOwnerRole(Long userId);

    boolean existsUserWithEmployeeRole(Long userId);

}
