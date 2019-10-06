package pl.piomin.services;

import java.util.Optional;

import io.micronaut.configuration.kafka.embedded.KafkaEmbedded;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderStatus;
import pl.piomin.services.model.OrderType;
import pl.piomin.services.repository.OrderInMemoryRepository;

import javax.inject.Inject;

@MicronautTest
@Property(name = "kafka.embedded.enabled", value = "true")
@Property(name = "kafka.bootstrap.servers", value = "localhost:9092")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderKafkaEmbeddedTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderKafkaEmbeddedTest.class);

    @Inject
    OrderClient client;
    @Inject
    OrderInMemoryRepository repository;
    @Inject
    OrderHolder orderHolder;
    @Inject
    KafkaEmbedded kafkaEmbedded;

    @BeforeAll
    public void init() {
		LOGGER.info("Topics: {}", kafkaEmbedded.getKafkaServer().get().zkClient().getAllTopicsInCluster());
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

	@Test
	@org.junit.jupiter.api.Order(2)
	public void testCancelTripOrder() throws InterruptedException {
		Order order = new Order(OrderType.CANCEL_TRIP, 1L, 50, 30);
		client.send(order);
		Order orderReceived = null;
		for (int i = 0; i < 10; i++) {
			orderReceived = orderHolder.getCurrentOrder();
			if (orderReceived != null)
				break;
			Thread.sleep(1000);
		}
		orderHolder.setCurrentOrder(null);
		Optional<Order> oo = repository.findById(1L);
		Assertions.assertTrue(oo.isPresent());
		Assertions.assertEquals(OrderStatus.REJECTED, oo.get().getStatus());
	}

	@Test
	@org.junit.jupiter.api.Order(3)
	public void testPaymentTripOrder() throws InterruptedException {
		Order order = new Order(OrderType.PAYMENT_PROCESSED, 1L, 50, 30);
		order.setTripId(1L);
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
		Optional<Order> oo = repository.findById(order.getId());
		Assertions.assertTrue(oo.isPresent());
		Assertions.assertEquals(OrderStatus.COMPLETED, oo.get().getStatus());
	}
}
