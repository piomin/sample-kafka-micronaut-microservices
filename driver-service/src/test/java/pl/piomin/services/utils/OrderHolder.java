package pl.piomin.services.utils;

import jakarta.inject.Singleton;
import pl.piomin.services.integration.Order;

@Singleton
public class OrderHolder {

	private Order currentOrder;

	public Order getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(Order currentOrder) {
		this.currentOrder = currentOrder;
	}

}
