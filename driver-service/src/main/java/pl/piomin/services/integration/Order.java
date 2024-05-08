package pl.piomin.services.integration;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private Long id;
    private LocalDateTime createdAt;
    private OrderType type;
    private Long userId;
    private Long tripId;
    private float currentLocationX;
    private float currentLocationY;

    public Order() {
    }

    public Order(OrderType type, Long userId, Long tripId) {
        this.createdAt = LocalDateTime.now();
        this.type = type;
        this.userId = userId;
        this.tripId = tripId;
    }

    public Order(Long id, OrderType type, Long userId, Long tripId, float currentLocationX, float currentLocationY) {
        this.createdAt = LocalDateTime.now();
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.tripId = tripId;
        this.currentLocationX = currentLocationX;
        this.currentLocationY = currentLocationY;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public float getCurrentLocationX() {
        return currentLocationX;
    }

    public void setCurrentLocationX(float currentLocationX) {
        this.currentLocationX = currentLocationX;
    }

    public float getCurrentLocationY() {
        return currentLocationY;
    }

    public void setCurrentLocationY(float currentLocationY) {
        this.currentLocationY = currentLocationY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", type=" + type +
                ", userId=" + userId +
                '}';
    }

}
