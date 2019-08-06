package pl.piomin.services;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderType;

import javax.inject.Inject;

@MicronautTest
public class OrderClientTest {

    @Inject
    OrderClient client;

    @Test
    public void testSend() {
        client.send(new Order(OrderType.NEW_TRIP, 1L, 50, 30));
    }

}
