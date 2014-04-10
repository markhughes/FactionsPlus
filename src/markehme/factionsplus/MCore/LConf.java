package markehme.factionsplus.MCore;

import com.massivecraft.mcore.store.Entity;

public class LConf extends Entity<LConf> {
	/*
	 * Meta 
	 */
	protected static transient LConf i;
	
	public static LConf get() { return i; }
	
	/*
	 * Fields
	 */
	
	public String language 			= "English";
	
	
	// ----------------------------
	// Command Descriptions
	// ----------------------------
	public String cmdDescAddWarp =  "create a faction warp, can be specified with a password";
	
	// ----------------------------
	// Warp related
	// ----------------------------
	public String warpsNotEnabled = "Warps are not enabled.";
	public String warpsPasswordTooSmall = "Your warp password must be at least 2 characters or more.";
	public String warpsNotHighEnoughRankingToSet = "<red>Your ranking is not high enough to create warps.";
	public String warpsNotInCorrectTerritory = "<red>You can not make warps in this territory.";
	public String warpsReachedMax = "<red>You have reach the maximum amount of warps.";
	
}
