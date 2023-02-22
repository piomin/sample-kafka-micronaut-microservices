package pl.piomin.services;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import pl.piomin.services.integration.Trip;

@KafkaClient
public interface TripClient {

    @Topic("trips")
    void send(@MessageBody Trip trip);

}
