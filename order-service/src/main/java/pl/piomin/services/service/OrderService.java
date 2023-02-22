package pl.piomin.services.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderStatus;
import pl.piomin.services.model.OrderType;
import pl.piomin.services.repository.OrderInMemoryRepository;

import java.util.Optional;

@Singleton
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private OrderInMemoryRepository repository;

    public OrderService(OrderInMemoryRepository repository) {
        this.repository = repository;
    }

    public void processPaymentProcessedOrder(Order order) {
        LOGGER.info("Processing: {}", order);
        Optional<Order> foundOrder = repository.findByTripIdAndType(order.getTripId(), OrderType.PAYMENT_PROCESSED);
        if (foundOrder.isPresent()) {
            Order o = foundOrder.get();
            o.setStatus(OrderStatus.COMPLETED);
            repository.update(o);
        } else {
            repository.add(order);
        }
    }

    public void processCancelTripOrder(Order order) {
        LOGGER.info("Processing: {}", order);
        Optional<Order> foundOrder = repository.findNewestByUserIdAndType(order.getUserId(), OrderType.NEW_TRIP);
        if (foundOrder.isPresent()) {
            Order o = foundOrder.get();
            o.setStatus(OrderStatus.REJECTED);
            repository.update(order);
        };
    }

}
