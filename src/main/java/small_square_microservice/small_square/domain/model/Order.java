package small_square_microservice.small_square.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Integer clientId;
    private LocalDateTime date;
    private String status;
    private Integer chefId;
    private Restaurant restaurant;
    private List<OrderDish> orderDishes;

    public Order(Long id, LocalDateTime date, Integer clientId, String status,
                 Integer chefId, Restaurant restaurant, List<OrderDish> orderDishes) {
        this.id = id;
        this.date = date;
        this.clientId = clientId;
        this.status = status;
        this.chefId = chefId;
        this.restaurant = restaurant;
        this.orderDishes = orderDishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getChefId() {
        return chefId;
    }

    public void setChefId(Integer chefId) {
        this.chefId = chefId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderDish> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(List<OrderDish> orderDishes) {
        this.orderDishes = orderDishes;
    }
}
