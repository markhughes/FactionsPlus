package markehme.factionsplus.MCore;

import com.massivecraft.massivecore.store.Entity;

public class MConf extends Entity<MConf> {
	
	/*
	 * Meta 
	 */
	protected static transient MConf i;
	
	public static MConf get() { return i; }
	
	/*
	 * Fields
	 */
	
	// Should we enable FactionsPlus at all?
	public Boolean enabled = true;
	
	// Show debug informations
	public Boolean debug = true;
	
	// Enable update checks?
	public Boolean updateCheck = true;
	
	// The Curse API Key
	public String CurseAPIKey = "";
	
	// Enable metrics?
	public Boolean metrics = true;
	
	// Scoreboard update time
	public long scoreboardUpdate = 5;
}
