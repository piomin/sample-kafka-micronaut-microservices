package pl.piomin.services;

import pl.piomin.services.integration.Order;
import pl.piomin.services.model.Driver;

import javax.inject.Singleton;

@Singleton
public class DriverHolder {

	private Driver currentDriver;

	public Driver getCurrentDriver() {
		return currentDriver;
	}

	public void setCurrentDriver(Driver currentDriver) {
		this.currentDriver = currentDriver;
	}

}
