package pl.piomin.services.repository;

import pl.piomin.services.model.Driver;
import pl.piomin.services.model.DriverStatus;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
public class DriverInMemoryRepository {

    Set<Driver> drivers = new HashSet<>();

    public Optional<Driver> findNearestDriver(final float currentLocationX, final float currentLocationY) {
        return drivers.stream()
            .filter(driver -> driver.getStatus() == DriverStatus.AVAILABLE)
            .min(Comparator.comparing(driver ->
                    Math.pow(driver.getCurrentLocationX() - currentLocationX, 2) +
                    Math.pow(driver.getCurrentLocationY() - currentLocationY, 2)));
    }

    public Optional<Driver> findById(Long id) {
        return drivers.stream().filter(driver -> driver.getId().equals(id)).findAny();
    }

    public void updateDriver(Driver driver) {
        drivers.remove(driver);
        drivers.add(driver);
    }

    @PostConstruct
    public void initializeData() {
        drivers.add(new Driver(1L, "Test1", 40, 50, DriverStatus.AVAILABLE));
        drivers.add(new Driver(2L, "Test2", 30, 50, DriverStatus.AVAILABLE));
        drivers.add(new Driver(3L, "Test3", 20, 50, DriverStatus.AVAILABLE));
        drivers.add(new Driver(4L, "Test4", 40, 60, DriverStatus.AVAILABLE));
        drivers.add(new Driver(5L, "Test5", 40, 70, DriverStatus.AVAILABLE));
        drivers.add(new Driver(6L, "Test6", 10, 10, DriverStatus.AVAILABLE));
        drivers.add(new Driver(7L, "Test7", 10, 80, DriverStatus.AVAILABLE));
        drivers.add(new Driver(8L, "Test8", 50, 30, DriverStatus.AVAILABLE));
        drivers.add(new Driver(9L, "Test9", 20, 50, DriverStatus.AVAILABLE));
        drivers.add(new Driver(10L, "Test10", 30, 50, DriverStatus.AVAILABLE));
    }

}
