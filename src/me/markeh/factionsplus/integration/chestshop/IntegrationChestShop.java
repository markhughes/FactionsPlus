package me.markeh.factionsplus.integration.chestshop;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationChestShop extends Integration {
	
	private static IntegrationChestShop instance = null;
	public static IntegrationChestShop get() {
		if (instance == null) instance = new IntegrationChestShop();
		
		return instance;
	}
	
	public IntegrationChestShop() {
		this.setPluginName("ChestShop");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationChestShopEvents());
	}
}
