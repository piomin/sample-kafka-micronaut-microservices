package pl.piomin.services;

import jakarta.inject.Singleton;

@Singleton
public class DriverHolder {

	private Driver currentDriver;

	Driver getCurrentDriver() {
		return currentDriver;
	}

	void setCurrentDriver(Driver currentDriver) {
		this.currentDriver = currentDriver;
	}

}
