package small_square_microservice.small_square.domain.model;

public class OrderDish {

    private Order order;
    private Dish dish;
    private Integer quantity;


    public OrderDish(Order order, Dish dish, Integer quantity) {
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
