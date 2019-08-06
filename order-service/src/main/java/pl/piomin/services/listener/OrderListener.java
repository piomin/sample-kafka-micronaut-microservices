package pl.piomin.services.listener;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Order;
import pl.piomin.services.service.OrderService;

@KafkaListener(groupId = "order")
public class OrderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderListener.class);

    private OrderService service;

    public OrderListener(OrderService service) {
        this.service = service;
    }

    @Topic("orders")
    public void receive(@Body Order order) {
        LOGGER.info("Received: {}", order);
        switch (order.getType()) {
            case PAYMENT_PROCESSED -> service.processPaymentProcessedOrder(order);
            case CANCEL_TRIP -> service.processCancelTripOrder(order);
        }
    }

}
