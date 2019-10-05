package pl.piomin.services.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderStatus;
import pl.piomin.services.model.OrderType;
import pl.piomin.services.repository.OrderInMemoryRepository;

import javax.inject.Singleton;
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
        foundOrder.ifPresentOrElse(localOrder -> {
            localOrder.setStatus(OrderStatus.COMPLETED);
            repository.update(localOrder);
        }, () -> repository.add(order));
    }

    public void processCancelTripOrder(Order order) {
        LOGGER.info("Processing: {}", order);
        Optional<Order> foundOrder = repository.findNewestByUserIdAndType(order.getUserId(), OrderType.NEW_TRIP);
        foundOrder.ifPresent(localOrder -> {
            localOrder.setStatus(OrderStatus.REJECTED);
            repository.update(order);
        });
    }

}
