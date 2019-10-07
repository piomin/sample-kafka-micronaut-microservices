package pl.piomin.services;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.github.dockerjava.api.command.CreateContainerCmd;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderType;
import pl.piomin.services.repository.OrderInMemoryRepository;

@MicronautTest
//@Testcontainers
@Property(name = "kafka.bootstrap.servers", value = "192.168.99.100:32835")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderKafkaContainerTest {

//	static Network network = Network.newNetwork();

//	@Container
//	public static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer()
//			.withEnv("KAFKA_ADVERTISED_LISTENERS", "192.168.99.100");
//	public static final GenericContainer ZOOKEEPER = new GenericContainer("wurstmeister/zookeeper")
//			.withCreateContainerCmdModifier(it -> ((CreateContainerCmd) it).withName("zookeeper").withHostName("zookeeper"))
//			.withExposedPorts(2181)
//			.withNetworkAliases("zookeeper")
//			.withNetwork(network);

//	@Container
//	public static final GenericContainer KAFKA_CONTAINER = new GenericContainer("wurstmeister/kafka")
//			.withCreateContainerCmdModifier(it -> ((CreateContainerCmd) it).withName("kafka").withHostName("kafka"))
//			.withExposedPorts(9092)
//			.withNetworkAliases("kafka")
//			.withEnv("KAFKA_ADVERTISED_HOST_NAME", "192.168.99.100")
//			.withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2181")
//			.withNetwork(network);

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderKafkaContainerTest.class);

	@Inject
	OrderClient client;
	@Inject
	OrderInMemoryRepository repository;
	@Inject
	OrderHolder orderHolder;

	private static EmbeddedServer server;

	@BeforeAll
	public static void setupServer() {
//		LOGGER.info("KAFKA_NET: {}", KAFKA_CONTAINER.getNetwork().getId());
//		server = ApplicationContext.run(EmbeddedServer.class, PropertySource.of("test",
//				CollectionUtils.mapOf("kafka.bootstrap.servers", "192.168.99.100:32835"))
//		);
//		server = ApplicationContext.run(EmbeddedServer.class, PropertySource.of("test",
//				CollectionUtils.mapOf("kafka.bootstrap.servers", KAFKA_CONTAINER.getContainerIpAddress() + ":" + KAFKA_CONTAINER.getMappedPort(9092)))
//		);
	}

	@AfterAll
	public static void stopServer() throws InterruptedException {
		if (server != null) {
			server.stop();
		}
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

}
