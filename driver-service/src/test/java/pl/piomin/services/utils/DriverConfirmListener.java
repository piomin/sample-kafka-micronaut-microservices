package pl.piomin.services.utils;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Driver;

@KafkaListener(groupId = "driverTest")
public class DriverConfirmListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DriverConfirmListener.class);

	DriverHolder driverHolder;

	public DriverConfirmListener(DriverHolder driverHolder) {
		this.driverHolder = driverHolder;
	}

	@Topic("drivers")
	public void receive(@MessageBody Driver driver) {
		LOGGER.info("Confirmed: {}", driver);
		driverHolder.setCurrentDriver(driver);
	}

}
