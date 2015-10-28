package me.markeh.factionsplus.integration.chestshop;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationChestShop extends Integration {
	public IntegrationChestShop() {
		this.setPluginName("ChestShop");
		
	}
	public void setup() {
		this.setEventsClass(new IntegrationChestShopEvents());
	}
}
