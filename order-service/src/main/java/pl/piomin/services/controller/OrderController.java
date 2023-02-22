package pl.piomin.services.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import pl.piomin.services.client.OrderClient;
import pl.piomin.services.model.Order;
import pl.piomin.services.repository.OrderInMemoryRepository;

import java.util.Set;

@Controller("orders")
public class OrderController {

    OrderInMemoryRepository repository;
    OrderClient client;

    public OrderController(OrderInMemoryRepository repository, OrderClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Post
    public Order add(@Body Order order) {
        order = repository.add(order);
        client.send(order);
        return order;
    }

    @Get
    public Set<Order> findAll() {
        return repository.findAll();
    }

}
