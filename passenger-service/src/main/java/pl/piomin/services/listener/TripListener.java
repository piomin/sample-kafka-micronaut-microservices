package pl.piomin.services.listener;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.integration.Trip;
import pl.piomin.services.service.PassengerService;

@KafkaListener(groupId = "passenger")
public class TripListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripListener.class);

    private PassengerService service;

    public TripListener(PassengerService service) {
        this.service = service;
    }

    @Topic("trips")
    public void receive(@MessageBody Trip trip) {
        LOGGER.info("Received: {}", trip);
        switch (trip.getStatus()) {
            case FINISHED -> service.processTripFinished(trip);
        }
    }

}
