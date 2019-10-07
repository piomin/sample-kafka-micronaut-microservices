package pl.piomin.services;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderType;
import pl.piomin.services.repository.OrderInMemoryRepository;

import javax.inject.Inject;

@MicronautTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderKafkaContainerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderKafkaContainerTest.class);

    @Container
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer();

    @Inject
    OrderClient client;
    @Inject
    OrderInMemoryRepository repository;
    @Inject
    OrderHolder orderHolder;
    private static EmbeddedServer server;
    ApplicationContext context;


    @BeforeAll
    public static void before() {
        server = ApplicationContext.run(EmbeddedServer.class, PropertySource.of("test", CollectionUtils.mapOf(
                "kafka.bootstrap.servers", "192.168.99.100:" + KAFKA_CONTAINER.getMappedPort(9092))
        ));
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testAddNewTripOrder() throws InterruptedException {
        Order order = new Order(OrderType.NEW_TRIP, 1L, 50, 30);
        order = repository.add(order);
        client.send(order);
        Order orderSent = null;
        for (int i = 0; i < 10; i++) {
            orderSent = orderHolder.getCurrentOrder();
            if (orderSent != null)
                break;
            Thread.sleep(1000);
        }
        orderHolder.setCurrentOrder(null);
        Assertions.assertNotNull(orderSent);
        Assertions.assertEquals(order.getId(), orderSent.getId());
    }

    @AfterAll
    public static void stopServer() throws InterruptedException {
        if (server != null) {
            server.stop();
        }
    }

}
