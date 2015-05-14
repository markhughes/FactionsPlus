package markehme.factionsplus.Cmds;

import java.util.ArrayList;
import java.util.List;

import markehme.factionsplus.EssentialsIntegration;
import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqWarpsEnabled;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FPPerm;
import markehme.factionsplus.extras.FType;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.SmokeUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdWarp extends FPCommand {
	public CmdWarp() {
		this.aliases.add("warp");
		
		this.fpidentifier = "warp";
		
		this.requiredArgs.add("name");
		this.optionalArgs.put("password", "string");
		this.optionalArgs.put("faction", "string");
		this.errorOnToManyArgs = false;

		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		this.addRequirements(ReqWarpsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.WARP.node));
		
		this.setHelp(LConf.get().cmdDescWarp);
		this.setDesc(LConf.get().cmdDescWarp);

	}

	@Override
	public void performfp() {
		String warpName = this.arg(0);
		String warpPass = null;

		Faction workingFaction;
		
		// Check if there is a password being supplied (- = no password)
		if(this.arg(1) != null) {
			if(this.arg(1) != "-") {
				warpPass = this.arg(1);
			}
		}
		
		// Check if a Faction is being supplied 
		if(this.arg(2) != null) {
			
			// Fetch the Faction
			workingFaction = Faction.get(this.arg(2));
			
			// Ensure it exists 
			if(workingFaction == null ) {
				msg(Txt.parse(LConf.get().warpsFactionNonExistant, this.arg(2)));
				return; 
			}
			
			// So if it's not another faction, and they're not an op or an admin..
			if(workingFaction.getId() != usender.getFactionId()
					&& (!usender.isUsingAdminMode() && !me.isOp()) ) {
				
				// Check for the factionsplus.warp.others permission 
				if(!FPPerm.WARPOTHERS.has(usender.getPlayer())) {
					msg(Txt.parse(LConf.get().warpsCantUseOtherFactions));
					return;
				}
			}
		} else {
			// Since no faction was supplied, assume it's their faction
			workingFaction = usender.getFaction();
			
			// Ensure that they're in a faction 
			if(workingFaction.isNone()) {
				msg(Txt.parse(LConf.get().warpsYouAreNotInAFaction));
				return;
			}
		}
		
		// Fetch Faction data - overwrite existing data
		fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(workingFaction.getId());
		
		// Ensure the faction has warps 
		if(fData.warpLocation.size() == 0) {
			msg(Txt.parse(LConf.get().warpsFactionHasNone));
			return;
		}
		
		// Ensure the warp exists 
		if(!fData.warpExists(warpName)) {
			msg(Txt.parse(LConf.get().warpsCantWarpNonExistant, warpName));
			return;
		}
		
		// Does the warp have a password? 
		if(fData.warpHasPassword(warpName)) {
			
			// Ensure a password was supplied
			if(warpPass == null) {
				msg(Txt.parse(LConf.get().warpsCantWarpNeedsPassword, warpName));
				return;
			} else {
				// Ensure the password is valid 
				if(!fData.warpValidatePassword(warpName, warpPass)) {
					msg(Txt.parse(LConf.get().warpsCantWarpBadPassword, warpPass, warpName));
					return;
				}
			}	
		}
		
		// Check the allowWarpFromOtherWorld configuration option
		if(fData.warpLocation.get(warpName.toLowerCase()).getWorld() != usender.getPlayer().getWorld().getName()
				&& !FPUConf.get(usender.getUniverse()).allowWarpFromOtherWorld ) {
			msg(Txt.parse(LConf.get().warpsWrongWorld));
			return;
		}
		
		// Ensure we can warp from this location ...
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("owned") && usender.isInOwnTerritory()) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}
		
		Faction factionIn = BoardColls.get().getFactionAt(PS.valueOf(usender.getPlayer().getLocation()));
		FType currentLocation = FType.valueOf(factionIn);
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("wilderness") && currentLocation == FType.WILDERNESS) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("safezone") && currentLocation == FType.SAFEZONE) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("warzone") && currentLocation == FType.WARZONE) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("ally") && factionIn.getRelationTo(usender).equals(Rel.ALLY)) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("enemy") && factionIn.getRelationTo(usender).equals(Rel.ENEMY)) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("neurtal") && factionIn.getRelationTo(usender).equals(Rel.NEUTRAL)) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpFrom.get("truce") && factionIn.getRelationTo(usender).equals(Rel.TRUCE)) {
			msg(Txt.parse(LConf.get().warpsCantWarpFromTerritory));
			return;
		}

		// Check if player can teleport from different world
		/*
		 * Move inside the try catch
		 * 
		 * if(!Conf.homesTeleportAllowedFromDifferentWorld && player.getWorld().getUID() != world){
		 * 		fme.msg("<b>You cannot teleport to your faction home while in a different world.");
		 * 		return;
		 * }
		 */

		Location loc = me.getLocation().clone();
		
		boolean doEnemyDistanceCheck = true;
		
		if(FPUConf.get(usender.getUniverse()).disallowWarpIfEnemyWithin > 0) {
			
			// All them if statements ..... (any convenient method to do this?)
			
			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for owned land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("owned")  && factionIn.getId() == usenderFaction.getId()) {
				doEnemyDistanceCheck = false;
			}
			
			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for ally land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("ally") && factionIn.getRelationTo(usender).equals(Rel.ALLY)) {
				doEnemyDistanceCheck = false;
			}
			
			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for enemy land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("enemy")  && factionIn.getRelationTo(usender).equals(Rel.ENEMY)) {
				doEnemyDistanceCheck = false;
			}

			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for neutral land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("neutral")  && factionIn.getRelationTo(usender).equals(Rel.NEUTRAL)) {
				doEnemyDistanceCheck = false;
			}
			
			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for truce land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("truce")  && factionIn.getRelationTo(usender).equals(Rel.TRUCE)) {
				doEnemyDistanceCheck = false;
			}
			
			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for safezone land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("safezone") && currentLocation == FType.SAFEZONE) {
				doEnemyDistanceCheck = false;
			}
			
			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for warzone land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("warzone") && currentLocation == FType.WARZONE) {
				doEnemyDistanceCheck = false;
			}
			
			// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for wilderness land
			if(FPUConf.get(usender.getUniverse()).ignoreDisallowWarpIfEnemyWithinIfIn.get("wilderness") && currentLocation == FType.WILDERNESS) {
				doEnemyDistanceCheck = false;
			}
			
			if(doEnemyDistanceCheck) { 
				// Commence the enemy distance check
				
				World w = loc.getWorld();
				double x = loc.getX();
				double y = loc.getY();
				double z = loc.getZ();

				for(Player checkingPlayer : me.getServer().getOnlinePlayers()) {
					
					// Ensure this is a valid player to check 
					if(checkingPlayer == null || !checkingPlayer.isOnline() || checkingPlayer.isDead() || checkingPlayer.getWorld() != w || checkingPlayer.getUniqueId() == me.getUniqueId())
						continue;
					
					
					// fetch the UPlayer of checkingPlayer
					UPlayer cuPlayer = UPlayer.get(checkingPlayer);
					
					// Ensure they're an enemy 
					if(!cuPlayer.getRelationTo(usender).equals(Rel.ENEMY))
						continue;
					
					
					Location l = checkingPlayer.getLocation();
					double dx = Math.abs(x - l.getX());
					double dy = Math.abs(y - l.getY());
					double dz = Math.abs(z - l.getZ());
					double max = FPUConf.get(usender.getUniverse()).disallowWarpIfEnemyWithin;

					// box-shaped distance check
					if (dx > max || dy > max || dz > max)
						continue;
					
					// enemy is too close - disallow the warp 
					msg(Txt.parse(LConf.get().warpsCantWarpEnemyTooClose, max));
					return;
				}
			}
			
		}
		
		PS warpToLocation = fData.warpLocation.get(warpName.toLowerCase());
		Faction factionTo = BoardColls.get().getFactionAt(warpToLocation);
		FType factionToType = FType.valueOf(factionIn);

		
		Boolean canHaveWarpHere = true;
		
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("owned")  && factionTo.getId() == usenderFaction.getId()) {
			canHaveWarpHere = false;
		}
		
		// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for ally land
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("ally") && factionTo.getRelationTo(usender).equals(Rel.ALLY)) {
			canHaveWarpHere = false;
		}
		
		// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for enemy land
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("enemy")  && factionTo.getRelationTo(usender).equals(Rel.ENEMY)) {
			canHaveWarpHere = false;
		}

		// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for neutral land
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("neutral")  && factionTo.getRelationTo(usender).equals(Rel.NEUTRAL)) {
			canHaveWarpHere = false;
		}
		
		// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for truce land
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("truce")  && factionTo.getRelationTo(usender).equals(Rel.TRUCE)) {
			canHaveWarpHere = false;
		}
		
		// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for safezone land
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("safezone") && factionToType == FType.SAFEZONE) {
			canHaveWarpHere = false;
		}
		
		// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for warzone land
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("warzone") && factionToType == FType.WARZONE) {
			canHaveWarpHere = false;
		}
		
		// Confirm the ignoreDisallowWarpIfEnemyWithinIfIn for wilderness land
		if(!FPUConf.get(usender.getUniverse()).allowWarpsIn.get("wilderness") && factionToType == FType.WILDERNESS) {
			canHaveWarpHere = false;
		}		
		
		// They can't have a warp here so don't allow them to warp to it + remove it 
		if(!canHaveWarpHere) {
			msg(Txt.parse(LConf.get().warpsCantWarpToTerritory));
			
			// Remove the warp if the configuration says so
			if(FPUConf.get(usender.getUniverse()).removeWarpIfInWrongTerritory) {
				fData.warpLocation.remove(warpName.toLowerCase());
				fData.warpPasswords.remove(warpName.toLowerCase());
				
				// Notify them that it's their fault the warps being removed.
				msg(Txt.parse(LConf.get().warpsCantWarpToTerritoryNotifyRemoved));
			}
			
			return;
		}
		
		
		// Take their money $$$
		if(FPUConf.get(usender.getUniverse()).economyCost.get("warp") > 0) {
			if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("warp"), usender)) {
				msg(Txt.parse(LConf.get().warpsCantWarpNotEnoughMoney, FPUConf.get(usender.getUniverse()).economyCost.get("warp")));
				return;
			}
		}
		
		Location warpTo = warpToLocation.asBukkitLocation();
		
		// Make a smoke effect if the server wants it
		if(FPUConf.get(usender.getUniverse()).smokeEffectOnWarp) {
			List<Location> smokeLocations = new ArrayList<Location>();
			smokeLocations.add(me.getLocation());
			smokeLocations.add(me.getLocation().add(0, 1, 0));
			smokeLocations.add(warpTo);
			smokeLocations.add(warpTo.clone().add(0, 1, 0));
			
			SmokeUtil.spawnCloudRandom(smokeLocations, 3f);
		}
		
		if(EssentialsIntegration.isHooked()) {
			try {	
				warpTo = EssentialsIntegration.getSafeDestination( warpTo );
			} catch(NoClassDefFoundError e) {
				FactionsPlus.severe("Sometimes Essentials has issues when it is reloaded (i.e. plugman, etc). Before reporting, please update Essentials, restart the server, and try again.");
				FactionsPlus.severe("Essentials is out of date (or we're not up to date). Can not get safe location for warp - just using normal location!");
			}
			
			try {
				if(EssentialsIntegration.handleTeleport(me, warpTo)) {
					msg(Txt.parse(LConf.get().warpsWarped, warpName));
					return;
				}
			} catch( Exception e) {				
				FactionsPlus.severe("Sometimes Essentials has issues when it is reloaded (i.e. plugman, etc). Before reporting, please update Essentials, restart the server, and try again.");
				FactionsPlus.severe("Essentials is out of date (or we're not up to date). Can not handle teleport for warp - handling ourselves!");
			}
			
		}
		
		me.teleport(warpTo);
		
	}
}
