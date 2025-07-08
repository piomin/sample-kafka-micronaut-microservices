package pl.piomin.services.repository;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import pl.piomin.services.model.Passenger;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
public class PassengerInMemoryRepository {

    private Set<Passenger> passengers = new HashSet<>();

    public Optional<Passenger> findById(Long id) {
        return passengers.stream().filter(passenger -> passenger.getId().equals(id)).findAny();
    }

    public void update(Passenger passenger) {
        passengers.remove(passenger);
        passengers.add(passenger);
    }

    @PostConstruct
    public void init() {
        passengers.add(new Passenger(1L, "Test1", "test1",  1000));
        passengers.add(new Passenger(2L,"Test2", "test2",  10));
        passengers.add(new Passenger(3L, "Test3", "test3",  1000));
        passengers.add(new Passenger(4L, "Test4", "test4",  100));
        passengers.add(new Passenger(5L, "Test5", "test5",  5000));
        passengers.add(new Passenger(6L, "Test6", "test6",  5000));
        passengers.add(new Passenger(7L, "Test7", "test7",  5000));
        passengers.add(new Passenger(8L, "Test8", "test8",  10));
    }

}
