package me.markeh.factionsplus.conf;

import java.util.List;

import me.markeh.factionsplus.FactionsPlus;
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
	@Option(section = "factionsplus", name = "debug", comment = "Show debug messages?") 
	public Boolean debug = false; 
	
	@Option(section = "factionsplus", name = "metrics", comment = "Enable metrics?") 
	public Boolean metrics = true; 
	
	// ---- Jails ---- //
	@Option(section = "jails", name = "enabled", comment = "Should jails be enabled?", pingOnUpdate = true) 
	public Boolean enableJails = false; 
	
	@Option(section = "jails", name = "warmup", comment = "Warm up time for the jail command (in seconds)") 
	public Integer jailWarmUp = 5;
	
	@Option(section = "jails", name = "minimumSentence", comment = "How long (in minutes) a player must be jailed for before they can be released") 
	public Integer jailsMinimumSentence = 3; 
	
	// ---- Warps ---- //
	@Option(section = "warps", name = "enabled", comment = "Should warps be enabled?") 
	public Boolean enableWarps = false;
	
	@Option(section = "warps", name = "distanceCheckForEnemies", comment = "Check the enemies (counted in blocks) in a distance and disallow using a warp if they're near by") 
	public Double warpsDistanceCheckForEnemies = 12.0;
	
	@Option(section = "warps", name = "distanceCheckForEnemiesTerritoryOverride", comment = "If true, the above option is ignore if they're in their own land") 
	public Boolean warpsDistanceCheckForEnemiesTerritoryOverride = true;
	
	@Option(section = "warps", name = "warmUp", comment = "Time (in seconds) before a player is teleported. Recommended minimum 6 seconds.") 
	public Integer warpsWarmUp = 6;
	
	@Option(section = "warps", name = "announce", comment = "Announce to Faction when warp is created or deleted") 
	public Boolean warpsAnnounce = true;
	
	// ---- Rules ---- //
	@Option(section = "rules", name = "enabled", comment = "Should rules be enabled?") 
	public Boolean enableRules = false;
	
	@Option(section = "rules", name = "max", comment = "Maximum rules per faction") 
	public Integer rulesMax = 10;
	
	// ---- Announcements ---- //
	@Option(section = "announcements", name = "enabled", comment = "Enable announements?") 
	public Boolean enableAnnouncements = false;
	
	@Option(section = "announcements", name = "cooldown", comment = "How long between announcement cooldowns in seconds") 
	public Integer announcementsCooldown = 5;
	
	// ---- Extras ---- //
	@Option(section = "commandblock", name = "inRadiusOf", comment = "Radius to check for enemy players to block a command") 
	public Double commandBlockInRadiusOf = 0.0;
	
	@Option(section = "commandblock", name = "commands", comment = "Commands to block") 
	public List<String> commandBlockCommands = Utils.get().getList("examplecommand1", "examplecommand2");
	
	@Option(section = "wildernessregen", name = "enabled", comment = "(unstable) Enable wilderness regeneration after a certain amount of time.", pingOnUpdate = true) 
	public Boolean wildernessregenEnabled = false;
	
	@Option(section = "wildernessregen", name = "timer", comment = "After how many milliseconds of inactivity should they be regenerated?") 
	public Long wildernessregenTimer = 43200000L;
		
	@Option(section = "wildernessregen", name = "useGriefManagementPlugin", comment = "If available, use a grief management plugin to roll back.") 
	public Boolean wildernessregenUseGriefManagementPlugin = true;

	// ---- Scoreboard ---- //
	@Option(section = "scoreboard", name = "enabled", comment = "(unstable) Enable the scoreboard?") 
	public Boolean scoreboard_enabled = false;
	
	@Option(section = "scoreboard", name = "update", comment = "Every x seconds we update the stats") 
	public Integer scoreboard_update = 25;
	
	@Option(section = "scoreboard", name = "menus", comment = "Which default menus should be enabled?") 
	public List<String> scoreboard_menus = Utils.get().getList("TopMembers", "TopPower");
	
	@Option(section = "scoreboard", name = "teams", comment = "Add players to scoreboard teams?") 
	public Boolean scoreboard_teams = false;
	
	@Option(section = "scoreboard", name = "disabled", comment = "Which worlds should all scoreboard features be disabled?") 
	public List<String> scoreboard_disabled = Utils.get().getList("gamelobby", "spleefarena1");
	
	// -----------------------
	//  Integration Specific 
	// -----------------------
	
	// ---- Chestshop Integration ---- //
	@Option(section = "chestShop", name = "allowInWilderness", comment = "Should we allow chest shops in wilderness?") 
	public Boolean chestShop_allowInWilderness = false;
		
	@Option(section = "chestShop", name = "allowInTerritory", comment = "Should we allow chest shops in their territory?") 
	public Boolean chestShop_allowInTerritory = false;
	
	@Option(section = "chestShop", name = "canUseEnemy", comment = "Should we allow enemies to use chest shops?") 
	public Boolean chestShop_canUseEnemy = false;
	
	// ---- Disguise Integration ---- //
	@Option(section = "disguiseIntegration", name = "allowInWilderness", comment = "Should we allow disguises in wilderness?") 
	public Boolean disguise_allowInWilderness = true;
	
	@Option(section = "disguiseIntegration", name = "allowEnemy", comment = "Should we allow disgusies in enemy territory?") 
	public Boolean disguise_allowInEnemy = true;
	
	@Option(section = "disguiseIntegration", name = "allowTerritory", comment = "Should we allow disgusies in their territory?") 
	public Boolean disguise_allowInTerritory = true;
	
	// ---- Cannons Integration ---- //
	@Option(section = "cannonsIntegration", name = "allowInWilderness", comment = "Allow building and using cannons in the wilderness?") 
	public Boolean cannons_allowInWilderness = true;
	
	@Option(section = "cannonsIntegration", name = "allowInTerritory", comment = "Allow building and using cannons in claimed territories?") 
	public Boolean cannons_allowInTerritories = true;
	
	// ---- Showcase Integration ---- //
	@Option(section = "showcase", name = "allowInWilderness", comment = "Should we allow showcases in wilderness?") 
	public Boolean showcase_allowInWilderness = false;
			
	@Option(section = "showcase", name = "allowInTerritory", comment = "Should we allow showcases in their territory?") 
	public Boolean showcase_allowInTerritory = false;
		
	@Option(section = "showcase", name = "canUseEnemy", comment = "Should we allow enemies to use showcases?") 
	public Boolean showcase_canUseEnemy = false;
		
	// -----------------------
	// Singleton
	// -----------------------
	
	private static Config instance;
	public static Config get() {
		if (instance == null) instance = new Config();
		
		return instance;
	}	
	
	@Override
	public final void postUpdatePing() {
		// On configuration update, we will remanage our listeners
		// just to ensure we only have the listeners enabled that
		// we need :-) 
		FactionsPlus.get().manageListeners();
	}
	
}
