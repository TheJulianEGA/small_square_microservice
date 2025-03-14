package small_square_microservice.small_square.infrastructure.security.service.jwt;

public interface IJwtService {

    String extractUsername(String jwt);

    String extractRole(String jwt);

}
