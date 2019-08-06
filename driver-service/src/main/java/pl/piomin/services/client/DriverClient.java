package pl.piomin.services.client;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import io.micronaut.messaging.annotation.Header;
import pl.piomin.services.model.Driver;

@KafkaClient
public interface DriverClient {

    @Topic("drivers")
    void send(@Body Driver driver, @Header("Order-Id") String orderId);

}
