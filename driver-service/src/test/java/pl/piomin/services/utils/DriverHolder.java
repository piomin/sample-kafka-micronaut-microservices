package pl.piomin.services.utils;

import jakarta.inject.Singleton;
import pl.piomin.services.model.Driver;

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
