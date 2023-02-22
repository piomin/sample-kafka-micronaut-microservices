package pl.piomin.services.listener;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.integration.Order;
import pl.piomin.services.service.PassengerService;

@KafkaListener(groupId = "passenger")
public class OrderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderListener.class);

    private PassengerService service;

    public OrderListener(PassengerService service) {
        this.service = service;
    }

    @Topic("orders")
    public void receive(@MessageBody Order order) {
        LOGGER.info("Received: {}", order);
        switch (order.getType()) {
            case NEW_TRIP -> service.processNewTripOrder(order);
        }
    }

}
