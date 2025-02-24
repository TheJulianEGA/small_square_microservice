package small_square_microservice.small_square.infrastructure.util;

public class InfrastructureConstants {

    public static final String USER_SERVICE_NAME = "user";
    public static final String USER_SERVICE_URL = "http://localhost:8080";
    public static final String RESTAURANT_NOT_FOUND = "Restaurant not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String DISH_NOT_FOUND = "Dish not found";
    public static final String AUTH_HEADER = "Authorization";
    public static final String ROLE_ADMINISTRATOR = "hasRole('ADMINISTRATOR')";
    public static final String ROLE_OWNER = "hasRole('OWNER')";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final int TOKEN_PREFIX_LENGTH = 7;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String NO_AUTHENTICATED_USER_ID_FOUND = "No authenticated user id found";

    private InfrastructureConstants() {
    }

}
