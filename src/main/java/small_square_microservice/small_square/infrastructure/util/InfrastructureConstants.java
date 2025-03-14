package small_square_microservice.small_square.infrastructure.util;

public class InfrastructureConstants {

    public static final String USER_SERVICE_NAME = "user";
    public static final String USER_SERVICE_URL = "http://localhost:8080";
    public static final String MESSAGE_SERVICE_NAME = "messaging";
    public static final String MESSAGE_SERVICE_URL = "http://localhost:8082";
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
    public static final String ROLE_CUSTOMER = "hasRole('CUSTOMER')";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_IN_PREPARATION = "in preparation";
    public static final String STATUS_READY = "ready";
    public static final String ROLE_EMPLOYEE = "hasRole('EMPLOYEE')";
    public static final String STATUS_DELIVERY = "delivery";
    public static final String ORDER_NOT_FOUND = "The order not found";
    public static final String ERROR_CONVERTING_USER_ID = "Error converting user ID.";
    public static final String TRACEABILITY_SERVICE_NAME = "traceability";
    public static final String TRACEABILITY_SERVICE_URL ="http://localhost:8083" ;
    public static final String EMAIL = "email";


    private InfrastructureConstants() {
    }

}
