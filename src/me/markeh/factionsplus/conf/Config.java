package me.markeh.factionsplus.conf;

import java.util.ArrayList;
import java.util.List;

import me.markeh.factionsplus.conf.obj.Configuration;
import me.markeh.factionsplus.conf.obj.Option;

public class Config extends Configuration {
	public Config() {
		this.setName("config");
	}
	
	// -----------------------
	//  Configuration Opts
	// -----------------------
	
	// ---- Jails ---- //
	@Option({"jails", "enabled", "Should jails be enabled?"}) 
	public Boolean enableJails = false; 
	
	@Option({"jails", "warmup", "Warm up time for the jail command (in seconds)"})
	public Integer jailWarmUp = 5;
	
	@Option({"jails", "minimumSentence", "How long (in minutes) a player must be jailed for before they can be released."})
	public Integer jailsMinimumSentence = 3; 
	
	// ---- Warps ---- //
	@Option({"warps", "enabled", "Should warps be enabled?"})
	public Boolean enableWarps = false;
	
	@Option({"warps", "distanceCheckForEnemies", "Check the enemies (counted in blocks) in a distance and disallow using a warp if they're near by"})
	public Double warpsDistanceCheckForEnemies = 12.0;
	
	@Option({"warps", "warmUp", "Time (in seconds) before a player is teleported. Recommended minimum 6 seconds."})
	public Integer warpsWarmUp = 6;
	
	// ---- Rules ---- //
	@Option({"rules", "enabled", "Should rules be enabled?"})
	public Boolean enableRules = false;
	
	@Option({"rules", "max", "Maximum rules per faction"})
	public Integer rulesMax = 10;
	
	// ---- Extras ---- //
	@Option({"commandblock", "inRadiusOf", "Radius to check for enemy players to block a command"}) 
	public Double commandBlockInRadiusOf = 15.0;
	
	@Option({"commandblock", "commands", "Commands to block (seperate with space and comma)"}) 
	public List<String> commandBlockCommands = new ArrayList<String>();
	
	
	// -----------------------
	//  Integration Specific 
	// -----------------------
	
	// ---- Chestshop ---- //
	@Option({"chestShop", "allowInWilderness", "Should we allow chest shops in wilderness?"}) 
	public Boolean chestShop_allowInWilderness = false;
		
	@Option({"chestShop", "allowInTerritory", "Shoudl we allow chest shops in their territory"}) 
	public Boolean chestShop_allowInTerritory = false;
	
	@Option({"chestShop", "canUseEnemy", "Should we allow enemies to use chest shops"}) 
	public Boolean chestShop_canUseEnemy = false;
	
		
	// -----------------------
	// Singleton
	// -----------------------
	
	private static Config instance;
	public static Config get() {
		if (instance == null) instance = new Config();
		
		return instance;
	}
}
