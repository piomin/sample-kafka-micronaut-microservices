package pl.piomin.services.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.integration.Order;
import pl.piomin.services.integration.OrderType;
import pl.piomin.services.integration.Trip;
import pl.piomin.services.model.Passenger;
import pl.piomin.services.repository.PassengerInMemoryRepository;

import java.util.Optional;

@Singleton
public class PassengerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerService.class);
    private static final int MIN_BALANCE = 50;

    private PassengerInMemoryRepository repository;
    private OrderClient orderClient;

    public PassengerService(PassengerInMemoryRepository repository, OrderClient orderClient) {
        this.repository = repository;
        this.orderClient = orderClient;
    }

    public void processTripFinished(Trip trip) {
        LOGGER.info("Processing: {}", trip);
        Optional<Passenger> passenger = repository.findById(trip.getPassengerId());
        passenger.ifPresent(localPassenger -> {
            localPassenger.setBalance(localPassenger.getBalance() - trip.getPrice());
            repository.update(localPassenger);
            Order order = new Order(OrderType.PAYMENT_PROCESSED, localPassenger.getId(), trip.getId());
            orderClient.send(order);
            LOGGER.info("Message sent: {}", order);
        });
    }

    public void processNewTripOrder(Order order) {
        LOGGER.info("Processing: {}", order);
        Optional<Passenger> passenger = repository.findById(order.getUserId());
        passenger.ifPresent(localPassenger -> {
            if (localPassenger.getBalance() < MIN_BALANCE) {
                Order cancelOrder = new Order(OrderType.CANCEL_TRIP, localPassenger.getId(), null);
                orderClient.send(cancelOrder);
                LOGGER.info("Message sent: {}", cancelOrder);
            }
        });
    }
}
