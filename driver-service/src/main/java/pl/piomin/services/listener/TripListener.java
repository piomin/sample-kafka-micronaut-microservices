package pl.piomin.services.listener;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.integration.Order;
import pl.piomin.services.integration.Trip;
import pl.piomin.services.service.DriverService;

@KafkaListener(groupId = "driver")
public class TripListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripListener.class);

    private DriverService service;

    public TripListener(DriverService service) {
        this.service = service;
    }

    @Topic("trips")
    public void receive(@Body Trip trip) {
        LOGGER.info("Received: {}", trip);
        switch (trip.getStatus()) {
            case FINISHED -> service.processTripFinished(trip);
            case REJECTED -> service.processTripRejected(trip);
        }
    }

}
