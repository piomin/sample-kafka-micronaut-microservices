package pl.piomin.services;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.model.Order;
import pl.piomin.services.model.OrderStatus;
import pl.piomin.services.model.OrderType;
import pl.piomin.services.repository.OrderInMemoryRepository;

import java.util.Optional;

//@MicronautTest
//@Testcontainers
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderKafkaContainerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderKafkaContainerTest.class);

//    @Container
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer()
            .withExposedPorts(9092);

//    static Network network = Network.newNetwork();
//
//	@Container
//	public static final GenericContainer ZOOKEEPER = new GenericContainer("wurstmeister/zookeeper")
//			.withCreateContainerCmdModifier(it -> ((CreateContainerCmd) it).withName("zookeeper").withHostName("zookeeper"))
//			.withExposedPorts(2181)
//			.withNetworkAliases("zookeeper")
//			.withNetwork(network);
//
//	@Container
//	public static final GenericContainer KAFKA_CONTAINER = new GenericContainer("wurstmeister/kafka")
//			.withCreateContainerCmdModifier(it -> ((CreateContainerCmd) it).withName("kafka").withHostName("kafka")
//                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(9092), new ExposedPort(9092))))
//			.withExposedPorts(9092)
//			.withNetworkAliases("kafka")
//			.withEnv("KAFKA_ADVERTISED_HOST_NAME", "192.168.99.100")
//			.withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2181")
//			.withNetwork(network);

//    @Inject
    OrderClient client;
//    @Inject
    OrderInMemoryRepository repository;
//    @Inject
    OrderHolder orderHolder;

//    @Test
//    @org.junit.jupiter.api.Order(1)
    public void testWaiting() throws InterruptedException {
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
        Assertions.assertNull(orderSent);
    }

//    @Test
//    @org.junit.jupiter.api.Order(2)
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

//    @Test
//    @org.junit.jupiter.api.Order(3)
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

}
