package markehme.factionsplus.MCore;


import com.massivecraft.massivecore.store.Entity;

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
	public String cmdDescAddWarp 		= "create a faction warp, can be specified with a password";
	public String cmdDescAnnounce		= "sends an announcment to your Faction";
	public String cmdDescBan			= "kicks a player out of your Faction, and stops them from re-joining";
	public String cmdDescClearLocks		= "clears LWC and Lockette locks not owned by a faction member in your land";
	public String cmdDescDebug			= "used to debug FactionsPlus";
	public String cmdDescFactionHome	= "teleport to another factions home";
	public String cmdDescFactionNeed	= "announces to Faction Leaders, and Officers that you're in need of a faction.";
	public String cmdDescJail			= "send a player to jail";
	public String cmdDescListWarps		= "list warps in a faction";
	public String cmdDescRemoveRule		= "remove a faction rule";
	public String cmdDescRemoveWarp		= "remove a warp";
	public String cmdDescListRules		= "list faction rules";
	public String cmdDescSetJail		= "sets the faction jail";
	public String cmdDescUnSetJail		= "removes the Jail location";
	public String cmdDescSetRule		= "sets faction rules";
	public String cmdDescToggleState	= "changes the faction between peaceful and normal";
	public String cmdDescUnBan			= "unbans a player allowing them to re-join the faction";
	public String cmdDescUnJail			= "removes a player from jail";
	public String cmdDescWarp			= "teleport to a warp";
	
	// ----------------------------
	// FactionsPlus specific related
	// ----------------------------
	public String fpNotEnabled 				= "<red>This command is unavailable while FactionsPlus is not enabled.";
	public String fpCantDamageThisMob		= "<red>You can't damage this mob type in this Faction land.";
	public String fpNoAlterRelationship		= "<red>You can't change the relationship to this Faction!";
	public String fpCommandDenied			= "<red>You can't run this command in this territory (%s)";
	public String fpKeepInvDie				= "<red>You get to keep your inventory in this territory (%s)";
	public String fpFactionPlayerJoin		= "%s from your Faction has joined.";
	public String fpFactionPlayerLeave		= "%s from your Faction has left the game.";
	
	// ----------------------------
	// Faction Command
	// ----------------------------
	public String factionCommandPlayerName		= "<a>{name}<aqua>(<green>{power}<aqua>)<a>";
	
	// ----------------------------
	// Warp related
	// ----------------------------
	public String warpsNotEnabled = "Warps are not enabled.";
	public String warpsPasswordTooSmall = "Your warp password must be at least 2 characters or more.";
	public String warpsNotHighEnoughRankingToModify = "<red>Your ranking is not high enough to modify warps.";
	public String warpsNotInCorrectTerritory = "<red>You can not make warps in this territory.";
	public String warpsReachedMax = "<red>You have reach the maximum amount of warps.";
	public String warpsAlreadyExists = "<red>This warp already exists.";
	public String warpsCreateSuccess = "<green>You have succesfuly created the warp %s";
	public String warpsCreateSuccessWithPassword = "<green>You have succesfuly created the warp %s with the password %s";
	public String warpsCreateNotify = "<gray>%s has created the warp %s for your faction.";
	public String warpsCanNotAfford = "<red>You need %s to create this warp.";
	public String warpsCanNotAffordDelete = "<red>You need %s to delete this warp.";
	public String warpsCantViewOthersWarps = "<red>You can not view other Factions warps";
	public String warpsCantViewListNonExistant = "<red>The Faction you're trying to list warps for does not exist.";
	public String warpsFactionHasNone = "<red>This Faction has no warps!";
	public String warpsListPrefix = "<orange>Warps:<white> %s";
	public String warpsCantDeleteNonExistant = "<red>This warp doesn't exist, so you can not remove it.";
	public String warpsNotifyRemoveSuccess = "<green>The warp <white>%s<green> was removed.";
	public String warpsFactionNonExistant = "<red>The faction '%s' does not exist!";
	public String warpsCantUseOtherFactions = "<red>You can't use warps owned by other factions!";
	public String warpsYouAreNotInAFaction = "<red>You are not in a faction.";
	public String warpsCantWarpNonExistant = "<red>The warp %s does not exist, so you can't warp to it.";
	public String warpsCantWarpNeedsPassword = "<red>This warp has a password, please access it using /f warp %s <password>";
	public String warpsCantWarpBadPassword = "<red>The password %s is incorrect for the warp %s";
	public String warpsCantWarpFromTerritory = "<red>You can not wrap in this territory.";
	public String warpsCantWarpEnemyTooClose = "<b>You cannot teleport to your faction warp while an enemy is within %s blocks of you.";
	public String warpsCantWarpToTerritory = "<red>This warp is no longer in the correct territory, so you can't warp here.";
	public String warpsCantWarpToTerritoryNotifyRemoved = "<red><b>This warp has now been removed.";
	public String warpsCantWarpNotEnoughMoney = "<red>You need %s to use this warp.";
	public String warpsWarped = "<green>Warping to %s ... ";
	public String warpsWrongWorld = "<red>You can't teleport to this warp, as it is in another world.";
	
	// ----------------------------
	// Announcement related
	// ----------------------------
	public String announcementMade = "<green>You announced: <white>%s";
	public String announcementNotHighEnoughRankingToSet = "<red>Your ranking is not high enough to make announcements.";
	public String announcementCanNotAfford = "<red>You need %s to create this announcement.";
	public String announcementNotify = "<red>%s <white>announced: %s";
	
	// ----------------------------
	// Ban related
	// ----------------------------
	public String banNotHighEnoughRanking = "<red>Your ranking is not high enough to ban members.";
	public String banCannotBan = "<red>You can't ban this player.";
	public String banPlayerAlreadyBanned = "<red>That player is already banned from your faction!";
	public String banCantBanLeader = "<red>You can't ban the leader of your faction!";
	public String banCantBanTooLowPower = "<red>The player you are trying to ban has too low power to leave.";
	public String banNotifyAll = "<red>%s <white>banned <red>%s> <white>from your faction!";
	public String banYouAreBanned = "<red>You can't join this Faction as you have been banned!";
	
	// ----------------------------
	// LWC Locks Related
	// ----------------------------
	public String LWCNoLocksFound = "<orange>No unlockable protections were found in this chunk";
	public String LWCLocksRemoved = "<green>Successfully removed %s protections from this chunk";
	public String LWCCantLockHere = "<red>You can lock only within your faction or unclaimed land!";
	public String LWCOnlyPeaceful = "<red>Only peaceful factions can make LWC protections!";
	
	// ----------------------------
	// FactionHome Related
	// ----------------------------
	public String factionHomeFactionNotFound = "<orange>This Faction can't be found.";
	public String factionHomeFactionNoHome = "<orange>That Faction doesn't have a home set!";
	public String factionHomeTeleportedTo = "<gold>You have been teleported to the Faction home of <red>%s";
	
	// ----------------------------
	// Faction Need Related
	// ----------------------------
	public String factionNeedAlreadyHaveFaction = "<red>You already have a faction!";
	public String factionNeedNotification = "<white>The player %s is looking for a Faction! You can invite them with <green>/f inv %s";
	public String factionNeedClarifySent = "<white>Factions have been notified!";
	public String factionNeedClarifyNoneSent = "<white>No Factions were notified, as no leaders or officers are online.";
	public String factionNeedAlreadyListening = "<red>You are already listening for players that need a Faction!";
	public String factionNeedAlreadyIgnoring = "<red>You are already ignoring players that need a Faction!";
	public String factionNeedNowListening = "<green>You are now listening for players that need a Faction!";
	public String factionNeedNowIgnoring = "<green>You are now listening for players that need a Faction!";
	public String factionNeedNotHighEnoughRankToToggle = "<red>Your rank is not high enough to toggle need requests.";
	
	// ----------------------------
	// Jail Related
	// ----------------------------
	public String jailsMustBeInOwnTerritoryToJail = "<red>You must be in your own faction territory to jail someone.";
	public String jailsNotHighEnoughRanking = "<red>Your ranking is not high enough to jail members.";
	public String jailsNotHighEnoughToModifyTheJail = "<red>Your ranking is not high enough to modify the jail.";
	public String jailsHasBeenRemoved = "<green>The jail location has been removed.";
	public String jailsOnlySetInOwnTerritory = "<red>Jails can only be set in your own faction land.";
	public String jailsCantAffordToJail = "<red>You need %s to send someone to jail.";
	public String jailsSetLocation = "<green>Your faction jail has been set to your current location.";
	public String jailsCantDoThatInJail = "<red>You can't do that! You're in jail.";
	public String jailsSpawnedIntoJail = "<red>You're currently in jail!";
	public String jailsCantTeleportInJail = "<red>You can't teleport away while jailed!";
	public String jailsPlayerNotApartOfFaction = "<red>%s is not apart of your faction!";
	public String jailsPlayerNeverOnServer = "<red>That player was never on this server!";
	public String jailsPlayerNotInJail = "<red>%s is not in jail!";
	public String jailsPlayerUnJailed = "<green>%s has been removed from jail.";
	
	
	// ----------------------------
	// Rules Related
	// ----------------------------
	public String rulesNotHighEnoughRankingToModify = "<red>Your ranking is not high enough to modify rules.";
	public String rulesErrorNotNumber = "%s is not a number.";
	public String rulesNotInFaction = "<b>You must be in a faction to do this.";
	public String rulesNotifyRemoved = "<green>This rule has been removed!";
	public String rulesNoRuleRemoved = "<b>No rule was removed - did it exist?";
	public String rulesListingStart = "<b>The rules for your faction are:";
	public String rulesHitMax = "<red>You have hit the maximum amount of rules for your faction (%s).";

	// ----------------------------
	// Protection Related
	// ----------------------------
	public String protectionCantUseVillager = "You can't use this villager in another factions territory.";
	public String protectionCantFishHere = "You can't fish in another factions territory.";
	public String protectionCantInteract = "You can not interact with that in this Factions land.";
	
	// ----------------------------
	// Toggle State Related
	// ----------------------------
	public String toggleStateNotOthers = "<red>Althought you can toggle the state of your own faction, you can't toggle the state of others.";
	public String toggleStateNotHighEnoughRankingToModify = "<red>Your ranking is not high enough to toggle state.";
	public String toggleStatePeacefulAdd = "<gold>You added the peaceful status to %s";
	public String toggleStatePeacefulRemove = "<gold>You removed the peaceful status to %s";
	public String toggleStateNonExistant = "<red>This faction does not exist, so you can't change it's status.";
	public String toggleStateCanAffordUp = "<red>You need %s to add the peaceful status.";
	public String toggleStateCanAffordDown = "<red>You need %s to remove the peaceful status.";
	
	// ----------------------------
	// Flying Related
	// ----------------------------
	public String flyCantUseSplashPotion = "<red>You can't use splash potions while flying!";
	public String flyCantAttack = "<red>You can't attack while flying!";
	
	// ----------------------------
	// Scoreboard Related
	// ----------------------------
	public String scoreboardTitle = "Top Factions";
	
	// ----------------------------
	// WorldGuard Region Related
	// ----------------------------
	public String worldGuardRegionInWay = "<red>There is a WorldGuard region in the way here!";
	
	// ----------------------------
	// Cannons Related
	// ----------------------------
	public String cannonsCantUseInEnemy = "<red>You can not use Cannons in enemy territory.";
	public String cannonsCantUseInOwn = "<red>You can't use cannons in your own territory.";
	public String cannonsCantUseInWilderness = "<red>You can't use cannons in the wilderness.";
	
	// ----------------------------
	// ChestShop Related
	// ----------------------------
	public String chestShopAutoRemoveNotice = "<gold>Automatically removed %s ChestShops protections in the claimed chunk.";
	public String chestShopErrorRemoving = "Could not remove ChestShop shops on land claim - please inform the administrator of this issue, and to check the console.";
	
	// ----------------------------
	// Lockette Related
	// ----------------------------
	public String locketteLocksRemoved = "<gold>Automatically removed %s Lockette protections in the claimed chunk.";
	
	// ----------------------------
	// Showcase Related
	// ----------------------------
	public String showcaseRemoved = "<gold>Automatically removed %s Showcase protections in the claimed chunk.";
	
	// ----------------------------
	// Disguise Related
	// ----------------------------
	public String disguisesCantDisguiseInOwnTerritory = "<red>You can't disguise in your own territory!";
	public String disguisesCantDisguiseInEnemyTerritory = "<red>You can't disguise in enemy territory!";
	public String disguisesUndisguised = "<red>You have been undisguised!";
	
	// ----------------------------
	// MultiVerse Portals Related
	// ----------------------------
	public String multiverseCantUseThisPortal = "<red>You can't use this portal in this land type.";
}

