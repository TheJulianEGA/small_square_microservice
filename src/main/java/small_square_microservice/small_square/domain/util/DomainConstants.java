package small_square_microservice.small_square.domain.util;

public class DomainConstants {

        public static final String USER_IS_NOT_OWNER = " the user is not the owner";
        public static final String USER_IS_NOT_EMPLOYEE =
                 """
                 the user is not the 'owner' please verify that everyone you entered has this role"
                """;
        public static final String RESTAURANT_NOT_FOUND = "Restaurant not found";
        public static final String CATEGORY_NOT_FOUND = "Category not found";
        public static final String DISH_NOT_FOUND = "Dish not found";
        public static final String USER_IS_NOT_OWNER_OF_THIS_RESTAURANT
                = "The user is not the owner of this restaurant";
        public static final String INVALID_PAGINATION_MESSAGE =
                """
                   The 'page' and 'size' values are incorrect. Make sure 'page' is greater than or equal to 0
                   and 'size' is greater than or equal to 1.
                   """;
        public static final String STATUS_PENDING = "pending";
        public static final String ORDER_IN_PROGRESS = "The order is already in progress";
        public static final String DISH_NOT_AVAILABLE = "Dish not available";
        public static final String EMPLOYEE_ALREADY_IN_RESTAURANT = "Employee already in restaurant";
        public static final String EMPLOYEE_ALREADY_IN_ANOTHER_RESTAURANT = "Employee already in another restaurant";


    private DomainConstants() {
        }
}
