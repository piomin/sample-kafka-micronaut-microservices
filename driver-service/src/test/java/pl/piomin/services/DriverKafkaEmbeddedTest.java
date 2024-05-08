package pl.piomin.services;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
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
import pl.piomin.services.utils.DriverHolder;
import pl.piomin.services.utils.OrderHolder;
import pl.piomin.services.utils.TripClient;

import java.util.Optional;
import java.util.Set;

//import javax.inject.Inject;


@MicronautTest
//@Property(name = "kafka.embedded.enabled", value = "true")
//@Property(name = "kafka.bootstrap.servers", value = "localhost:9092")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DriverKafkaEmbeddedTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverKafkaEmbeddedTest.class);

    @Inject
    DriverInMemoryRepository repository;
    @Inject
    OrderHolder orderHolder;
    @Inject
    DriverHolder driverHolder;
    @Inject
    OrderClient orderClient;
    @Inject
    TripClient tripClient;

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testWaiting() throws InterruptedException {
        LOGGER.info("testWaiting: BEGIN");
        Order o = new Order(OrderType.NEW_DRIVER, 1L, 1L);
        orderClient.send(o);
        LOGGER.info("testWaiting: Order Sent -> {}", o);
        Driver driverSent = null;
        for (int i = 0; i < 10; i++) {
            driverSent = driverHolder.getCurrentDriver();
            if (driverSent != null)
                break;
            Thread.sleep(1000);
        }
        driverHolder.setCurrentDriver(null);
        Assertions.assertNull(driverSent);
        Set<Driver> drivers = repository.findByStatus(DriverStatus.UNAVAILABLE);
        Assertions.assertEquals(0, drivers.size());
        LOGGER.info("testWaiting: FINISHED");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testFinishTrip() throws InterruptedException {
        LOGGER.info("testFinishTrip: BEGIN");
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
        LOGGER.info("testFinishTrip: FINISHED");
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void testNewTrip() throws InterruptedException {
        LOGGER.info("testNewTrip: BEGIN");
        Order o = new Order(1L, OrderType.NEW_TRIP, 2L, 2L, 30.0F, 50.0F);
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
        LOGGER.info("testNewTrip: FINISHED");
    }

}
