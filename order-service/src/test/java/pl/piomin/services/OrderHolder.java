package pl.piomin.services;

import jakarta.inject.Singleton;
import pl.piomin.services.model.Order;

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
