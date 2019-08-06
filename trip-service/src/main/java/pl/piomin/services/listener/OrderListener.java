package pl.piomin.services.listener;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.integration.Order;
import pl.piomin.services.service.TripService;

@KafkaListener(groupId = "trip")
public class OrderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderListener.class);

    private TripService service;

    public OrderListener(TripService service) {
        this.service = service;
    }

    @Topic("orders")
    public void receive(@Body Order order) {
        LOGGER.info("Received: {}", order);
        switch (order.getType()) {
            case NEW_TRIP -> service.processNewTripOrder(order);
            case END_TRIP -> service.processEndTripOrder(order);
            case PAYMENT_PROCESSED -> service.processPaymentProcessedOrder(order);
            case CANCEL_TRIP -> service.processCancelTripOrder(order);
        }
    }

}
