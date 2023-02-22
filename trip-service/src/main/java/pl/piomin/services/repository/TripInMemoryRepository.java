package pl.piomin.services.repository;

import jakarta.inject.Singleton;
import pl.piomin.services.model.Trip;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
public class TripInMemoryRepository {

    private Set<Trip> trips = new HashSet<>();

    public Trip add(Trip trip) {
        trip.setId((long) (trips.size() + 1));
        trips.add(trip);
        return trip;
    }

    public Optional<Trip> findById(Long tripId) {
        return trips.stream().filter(trip -> trip.getId().equals(tripId)).findAny();
    }

    public Optional<Trip> findNewestByPassengerId(Long passengerId) {
        return trips.stream().filter(trip -> trip.getPassengerId().equals(passengerId))
                .max(Comparator.comparing(Trip::getId));
    }

    public Optional<Trip> findByOrderId(Long orderId) {
        return trips.stream().filter(trip -> trip.getOrderId().equals(orderId)).findAny();
    }

    public void update(Trip trip) {
        trips.remove(trip);
        trips.add(trip);
    }

}
