package pl.piomin.services;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Driver;
import pl.piomin.services.repository.DriverInMemoryRepository;

import javax.inject.Inject;
import java.util.Optional;

@MicronautTest
public class DriverRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverRepositoryTest.class);

    @Inject
    DriverInMemoryRepository repository;

    @Test
    public void testFindNearest() {
        Optional<Driver> driver = repository.findNearestDriver(40, 30);
        driver.ifPresent(driver1 -> LOGGER.info("Driver found: {}, {}", driver1.getCurrentLocationX(), driver1.getCurrentLocationY()));
    }
}
