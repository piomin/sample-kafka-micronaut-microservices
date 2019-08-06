package pl.piomin.services.client;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import pl.piomin.services.model.Order;

@KafkaClient
public interface OrderClient {

    @Topic("orders")
    void send(@Body Order order);

}
