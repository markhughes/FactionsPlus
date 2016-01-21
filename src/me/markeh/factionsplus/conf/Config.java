package me.markeh.factionsplus.conf;

import java.util.List;

import me.markeh.factionsplus.conf.obj.Configuration;
import me.markeh.factionsplus.conf.obj.Option;
import me.markeh.factionsplus.util.Utils;

public class Config extends Configuration<Config> {
	
	public Config() {
		this.setName("config");
		this.setHeader(
			"FactionsPlus Configuration",
			"-----------------------------------------------",
			"This file is well documented for the configuration options throughout"
		);
	}
	
	// -----------------------
	//  Configuration Options
	// -----------------------

	// ---- FactionsPlus Specific ---- //
	@Option({"factionsplus", "debug", "Show debug messages?"}) 
	public Boolean debug = false; 
	
	@Option({"factionsplus", "metrics", "Enable metrics?"}) 
	public Boolean metrics = true; 
	
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
	
	@Option({"warps", "announce", "Announce to Faction when warp is created or deleted"})
	public Boolean warpsAnnounce = true;
	
	// ---- Rules ---- //
	@Option({"rules", "enabled", "Should rules be enabled?"})
	public Boolean enableRules = false;
	
	@Option({"rules", "max", "Maximum rules per faction"})
	public Integer rulesMax = 10;
	
	// ---- Announcements ---- //
	@Option({"announcements", "enabled", "Enable announements?"})
	public Boolean enableAnnouncements = false;
	
	@Option({"announcements", "cooldown", "How long between announcement cooldowns in seconds"})
	public Integer announcementsCooldown = 5;
	
	// ---- Extras ---- //
	@Option({"commandblock", "inRadiusOf", "Radius to check for enemy players to block a command"}) 
	public Double commandBlockInRadiusOf = 0.0;
	
	@Option({"commandblock", "commands", "Commands to block"}) 
	public List<String> commandBlockCommands = Utils.get().getList("examplecommand1", "examplecommand2");
	
	@Option({"wildernessregen", "enabled", "(unstable) Should we enable wilderness chunk regeneration?"}) 
	public Boolean wildernessregenEnabled = false;
	
	@Option({"wildernessregen", "timer", "(unstable) After how many milliseconds of inactivity should they be regenerated?"}) 
	public Long wildernessregenTimer = 43200000L;
		
	// ---- Scoreboard ---- //
	@Option({"scoreboard", "enabled", "Enable the scoreboard?"}) 
	public Boolean scoreboard_enabled = false;
	
	@Option({"scoreboard", "update", "Every x seconds we update the stats"}) 
	public Integer scoreboard_update = 25;
	
	@Option({"scoreboard", "menus", "Which default menus should be enabled?"}) 
	public List<String> scoreboard_menus = Utils.get().getList("TopMembers", "TopPower");
	
	@Option({"scoreboard", "teams", "Add players to scoreboard teams "}) 
	public Boolean scoreboard_teams = false;
	
	@Option({"scoreboard", "disabled", "Which worlds should all scoreboard features be disabled?"}) 
	public List<String> scoreboard_disabled = Utils.get().getList("gamelobby", "spleefarena1");
	
	// -----------------------
	//  Integration Specific 
	// -----------------------
	
	// ---- Chestshop Integration ---- //
	@Option({"chestShop", "allowInWilderness", "Should we allow chest shops in wilderness?"}) 
	public Boolean chestShop_allowInWilderness = false;
		
	@Option({"chestShop", "allowInTerritory", "Should we allow chest shops in their territory?"}) 
	public Boolean chestShop_allowInTerritory = false;
	
	@Option({"chestShop", "canUseEnemy", "Should we allow enemies to use chest shops?"}) 
	public Boolean chestShop_canUseEnemy = false;
	
	// ---- Disguise Integration ---- //
	@Option({"disguiseIntegration", "allowInWilderness", "Should we allow disguises in wilderness?"}) 
	public Boolean disguise_allowInWilderness = true;
	
	@Option({"disguiseIntegration", "allowEnemy", "Should we allow disgusies in enemy territory?"}) 
	public Boolean disguise_allowInEnemy = true;
	
	@Option({"disguiseIntegration", "allowTerritory", "Should we allow disgusies in their territory?"}) 
	public Boolean disguise_allowInTerritory = true;
	
	// ---- Cannons Integration ---- //
	@Option({"cannonsIntegration", "allowInWilderness", "Allow building and using cannons in the wilderness?"}) 
	public Boolean cannons_allowInWilderness = true;
	
	@Option({"cannonsIntegration", "allowInTerritory", "Allow building and using cannons in claimed territories?"}) 
	public Boolean cannons_allowInTerritories = true;
	
	// ---- Showcase Integration ---- //
	@Option({"showcase", "allowInWilderness", "Should we allow showcases in wilderness?"}) 
	public Boolean showcase_allowInWilderness = false;
			
	@Option({"showcase", "allowInTerritory", "Should we allow showcases in their territory?"}) 
	public Boolean showcase_allowInTerritory = false;
		
	@Option({"showcase", "canUseEnemy", "Should we allow enemies to use showcases?"}) 
	public Boolean showcase_canUseEnemy = false;
		
	// -----------------------
	// Singleton
	// -----------------------
	
	private static Config instance;
	public static Config get() {
		if (instance == null) instance = new Config();
		
		return instance;
	}
}
