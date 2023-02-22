package pl.piomin.services.listener;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import io.micronaut.messaging.annotation.MessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.integration.Driver;
import pl.piomin.services.service.TripService;

@KafkaListener(groupId = "trip")
public class DriverListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderListener.class);

    private TripService service;

    public DriverListener(TripService service) {
        this.service = service;
    }

    @Topic("drivers")
    public void receive(@MessageBody Driver driver, @MessageHeader("Order-Id") String orderId) {
        LOGGER.info("Received: driver->{}, header->{}", driver, orderId);
        service.processNewDriver(driver, orderId);
    }

}
