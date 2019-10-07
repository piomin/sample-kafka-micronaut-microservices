package pl.piomin.services;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import io.micronaut.messaging.annotation.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.integration.Order;
import pl.piomin.services.listener.OrderListener;
import pl.piomin.services.model.Driver;

import javax.inject.Inject;

@KafkaListener(groupId = "driverTest")
public class DriverConfirmListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DriverConfirmListener.class);

	@Inject
	DriverHolder driverHolder;

	@Topic("orders")
	public void receive(@Body Driver driver) {
		LOGGER.info("Confirmed: {}", driver);
		driverHolder.setCurrentDriver(driver);
	}

}
