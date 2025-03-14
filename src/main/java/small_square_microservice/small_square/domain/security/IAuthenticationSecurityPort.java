package small_square_microservice.small_square.domain.security;

public interface IAuthenticationSecurityPort {

    Long getAuthenticatedUserId();

    String getAuthenticatedUserEmail();

}
