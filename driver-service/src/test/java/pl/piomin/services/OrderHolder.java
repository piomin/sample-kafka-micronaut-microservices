package pl.piomin.services;

import pl.piomin.services.integration.Order;

import javax.inject.Singleton;

@Singleton
public class OrderHolder {

	private Order currentOrder;

	Order getCurrentOrder() {
		return currentOrder;
	}

	void setCurrentOrder(Order currentOrder) {
		this.currentOrder = currentOrder;
	}

}
