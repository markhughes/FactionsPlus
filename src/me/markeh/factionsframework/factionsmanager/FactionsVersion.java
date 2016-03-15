package me.markeh.factionsframework.factionsmanager;

public enum FactionsVersion {
	FactionsUUID, Factions2_6, Factions2_8_6, Factions2_X

	;
	
	public boolean requiresMassiveCore() {
		if (this.toString().startsWith("Factions2_")) return true;
		return false;	
	}
	
	public boolean isFactions1_6UUID() {
		return this == FactionsUUID;
	}
}
