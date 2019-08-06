package pl.piomin.services.repository;

import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderType;

import javax.inject.Singleton;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
public class OrderInMemoryRepository {

    private Set<Order> orders = new HashSet<>();

    public Order add(Order order) {
        order.setId((long) (orders.size() + 1));
        orders.add(order);
        return order;
    }

    public void update(Order order) {
        orders.remove(order);
        orders.add(order);
    }

    public Optional<Order> findByTripIdAndType(Long tripId, OrderType type) {
        return orders.stream().filter(order -> order.getTripId().equals(tripId) && order.getType() == type).findAny();
    }

    public Optional<Order> findNewestByUserIdAndType(Long userId, OrderType type) {
        return orders.stream().filter(order -> order.getUserId().equals(userId) && order.getType() == type)
                .max(Comparator.comparing(Order::getId));
    }

    public Set<Order> findAll() {
        return orders;
    }

}
