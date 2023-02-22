package pl.piomin.services;

import javax.inject.Inject;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderType;
import pl.piomin.services.repository.OrderInMemoryRepository;

//@MicronautTest
//@Testcontainers
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderIntegrationTests {


	private static final Logger LOGGER = LoggerFactory.getLogger(OrderKafkaContainerTest.class);

	static Network network = Network.newNetwork();

//	@Container
	public static final GenericContainer ZOOKEEPER = new GenericContainer("wurstmeister/zookeeper")
			.withCreateContainerCmdModifier(it -> ((CreateContainerCmd) it).withName("zookeeper").withHostName("zookeeper"))
			.withExposedPorts(2181)
			.withNetworkAliases("zookeeper")
			.withNetwork(network);

//	@Container
	public static final GenericContainer KAFKA_CONTAINER = new GenericContainer("wurstmeister/kafka")
			.withCreateContainerCmdModifier(it -> ((CreateContainerCmd) it).withName("kafka").withHostName("kafka")
					.withPortBindings(new PortBinding(Ports.Binding.bindPort(9092), new ExposedPort(9092))))
			.withExposedPorts(9092)
			.withNetworkAliases("kafka")
			.withEnv("KAFKA_ADVERTISED_HOST_NAME", "192.168.99.100")
			.withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2181")
			.withNetwork(network);

//	@Container
//	public static final GenericContainer DRIVER_CONTAINER = new GenericContainer("piomin/driver-service")
//			.withNetwork(network);

//	@Inject
	OrderClient client;
//	@Inject
	OrderInMemoryRepository repository;
//	@Inject
	DriverHolder driverHolder;

//	@Test
//	@org.junit.jupiter.api.Order(1)
	public void testWaiting() throws InterruptedException {
		Order order = new Order(OrderType.NEW_TRIP, 1L, 50, 30);
		order = repository.add(order);
		client.send(order);
		Driver driverSent = null;
		for (int i = 0; i < 10; i++) {
			driverSent = driverHolder.getCurrentDriver();
			if (driverSent != null)
				break;
			Thread.sleep(1000);
		}
		driverHolder.setCurrentDriver(null);
		Assertions.assertNull(driverSent);
	}

//	@Test
//	@org.junit.jupiter.api.Order(1)
	public void testNewTrip() throws InterruptedException {
		Order order = new Order(OrderType.NEW_TRIP, 1L, 50, 30);
		order = repository.add(order);
		client.send(order);
		Driver driverReceived = null;
		for (int i = 0; i < 10; i++) {
			driverReceived = driverHolder.getCurrentDriver();
			if (driverReceived != null)
				break;
			Thread.sleep(1000);
		}
		driverHolder.setCurrentDriver(null);
		Assertions.assertNotNull(driverReceived);
	}

}
