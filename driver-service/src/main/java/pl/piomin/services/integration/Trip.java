package pl.piomin.services.integration;

import java.time.LocalDateTime;
import java.util.Objects;

public class Trip {

    private Long id;
    private float locationX;
    private float locationY;
    private String destination;
    private Long passengerId;
    private Long driverId;
    private int price;
    private TripStatus status;
    private LocalDateTime startTime;

    public Trip() {

    }

    public Trip(float locationX, float locationY, String destination, Long passengerId) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.destination = destination;
        this.passengerId = passengerId;
        this.startTime = LocalDateTime.now();
        this.status = TripStatus.NEW;
    }

    public Trip(Long id, float locationX, float locationY, Long passengerId, Long driverId) {
        this.id = id;
        this.locationX = locationX;
        this.locationY = locationY;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.startTime = LocalDateTime.now();
        this.status = TripStatus.NEW;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id.equals(trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", destination='" + destination + '\'' +
                ", passengerId=" + passengerId +
                ", driverId=" + driverId +
                ", price=" + price +
                ", status=" + status +
                ", startTime=" + startTime +
                '}';
    }
}
