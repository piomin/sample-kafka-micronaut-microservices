package pl.piomin.services;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener(groupId = "driverOrderTest")
public class DriverConfirmListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DriverConfirmListener.class);

	DriverHolder driverHolder;

	public DriverConfirmListener(DriverHolder driverHolder) {
		this.driverHolder = driverHolder;
	}

	@Topic("orders")
	public void receive(@MessageBody Driver driver) {
		LOGGER.info("Confirmed: {}", driver);
		driverHolder.setCurrentDriver(driver);
	}

}
