package pl.piomin.services;

import javax.inject.Inject;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.listener.OrderListener;
import pl.piomin.services.model.Order;

@KafkaListener(groupId = "orderTest")
public class OrderConfirmListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderListener.class);

	@Inject
	OrderHolder orderHolder;

	@Topic("orders")
	public void receive(@Body Order order) {
		LOGGER.info("Confirmed: {}", order);
		orderHolder.setCurrentOrder(order);
	}

}
