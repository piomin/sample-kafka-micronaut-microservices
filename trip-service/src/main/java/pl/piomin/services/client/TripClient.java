package pl.piomin.services.client;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import pl.piomin.services.model.Trip;

@KafkaClient
public interface TripClient {

    @Topic("trips")
    void send(@MessageBody Trip trip);

}
