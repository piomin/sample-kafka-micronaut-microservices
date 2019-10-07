package pl.piomin.services;

import io.micronaut.configuration.kafka.embedded.KafkaEmbedded;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.integration.Order;
import pl.piomin.services.integration.OrderType;
import pl.piomin.services.integration.Trip;
import pl.piomin.services.integration.TripStatus;
import pl.piomin.services.model.Driver;
import pl.piomin.services.model.DriverStatus;
import pl.piomin.services.repository.DriverInMemoryRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@MicronautTest
@Property(name = "kafka.embedded.enabled", value = "true")
@Property(name = "kafka.bootstrap.servers", value = "localhost:9092")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DriverKafkaEmbeddedTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverKafkaEmbeddedTest.class);

    @Inject
    DriverInMemoryRepository repository;
    @Inject
    KafkaEmbedded kafkaEmbedded;
    @Inject
    OrderHolder orderHolder;
    @Inject
    DriverHolder driverHolder;
    @Inject
    OrderClient orderClient;
    @Inject
    TripClient tripClient;

    @BeforeAll
    public void init() {
        LOGGER.info("Topics: {}", kafkaEmbedded.getKafkaServer().get().zkClient().getAllTopicsInCluster());
    }

//    @Test
//    @org.junit.jupiter.api.Order(1)
    public void testNewTrip() throws InterruptedException {
        Order o = new Order(OrderType.NEW_TRIP, 1L, 1L);
        orderClient.send(o);
        Driver driverSent = null;
        for (int i = 0; i < 10; i++) {
            driverSent = driverHolder.getCurrentDriver();
            if (driverSent != null)
                break;
            Thread.sleep(1000);
        }
        driverHolder.setCurrentDriver(null);
        Assertions.assertNotNull(driverSent);
        Set<Driver> drivers = repository.findByStatus(DriverStatus.UNAVAILABLE);
        Assertions.assertEquals(1, drivers.size());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testFinishTrip() throws InterruptedException {
        Trip trip = new Trip(1L, 30.0F, 40.0F, 1L, 1L);
        trip.setStatus(TripStatus.FINISHED);
        tripClient.send(trip);
        Order orderSent = null;
        for (int i = 0; i < 10; i++) {
            orderSent = orderHolder.getCurrentOrder();
            if (orderSent != null)
                break;
            Thread.sleep(1000);
        }
        orderHolder.setCurrentOrder(null);
        Optional<Driver> driver = repository.findById(trip.getDriverId());
        Assertions.assertTrue(driver.isPresent());
        Assertions.assertEquals(DriverStatus.AVAILABLE, driver.get().getStatus());
        Assertions.assertNotNull(orderSent);
        Assertions.assertEquals(1L, orderSent.getTripId());
    }

}
