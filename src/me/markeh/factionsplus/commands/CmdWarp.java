package me.markeh.factionsplus.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.Utils;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdWarp extends FactionsCommand {

	public CmdWarp() {
		this.aliases.add("warp");
		this.description = "Teleport to a faction warp";
		
		this.requiredArguments.add("name");
		this.optionalArguments.put("password", "none");
		
		this.requiredPermissions.add(Perm.get("factionsplus.warp", "You don't have permission to do that."));
		
		this.mustHaveFaction = true;
		
	}
	
	@Override
	public void run() {
		if ( ! this.isPlayer() ) {
			msg(Texts.playerOnlyCommand);
			return; 
		}
		
		final FactionData fdata = FactionData.get(this.faction.getID());
		
		String warpName = getArg(0).toLowerCase();
		
		if ( ! fdata.warpLocations.containsKey(warpName)) {
			msg("<red>The warp " + getArg(0) + " does not exist!");
			
			String maybe = fdata.warpLocations.findKeyLike(warpName);
			
			if (maybe != null) msg(ChatColor.GRAY + "Maybe you meant " + ChatColor.WHITE + maybe);
			
			return;
		}
		
		String optionalPassword = null;
		
		if (getArg(1) != null) {
			optionalPassword = Utils.get().MD5(getArg(1).toLowerCase().trim());
		}
		
		if (fdata.warpPasswords.get(warpName) != null) {
			if (optionalPassword == null) {
				msg("<red>This warp has a password. Please specify a password!");
				return;
			}
			
			if ( ! fdata.warpPasswords.get(warpName).trim().equals(optionalPassword.trim())) {
				msg("<red>The password you specified does not match.");
				return;
			}
		}
		
		
		if (this.factions.getFactionAt(fdata.warpLocations.get(warpName).getBukkitLocation()).getID() != this.faction.getID()) {
			fdata.warpLocations.remove(warpName);
			fdata.warpPasswords.remove(warpName);
			
			msg("<red>That warp is no longer in your faction land and has been removed.");
			
			return;
		}
		
		
		if (Config.get().warpsDistanceCheckForEnemies > 0) {			
			double radiusSquared = Config.get().warpsDistanceCheckForEnemies*Config.get().warpsDistanceCheckForEnemies;
		 
			for (Player otherPlayer : FactionsPlus.get().getServer().getOnlinePlayers()) {
				if (otherPlayer.getLocation().distanceSquared(player.getLocation()) <= radiusSquared) {
					FPlayer fOtherPlayer = FPlayer.get(otherPlayer);
					if (fOtherPlayer.getFaction().isEnemyOf(faction)) {
						msg("<red>There is an enemy nearby, you can't warp right now.");
						return;
					}
				}
			}
		}
		
		final FPlayer warper = this.fplayer;
		
		if (Config.get().warpsWarmUp > 0) {
			msg("<gold>Warping you in %s seconds.".replace("%s", Config.get().warpsWarmUp + "")); 
			
			warper.warmUpTask(
				Config.get().warpsWarmUp,
				new Runnable() {
					@Override
					public void run() {
						warper.getPlayer().teleport(fdata.warpLocations.get(warpName).getBukkitLocation());
						warper.msg("<gold>Taking you to <aqua>%s".replace("%s", getArg(0))); 
					}
				}, 
				new Runnable() {
					@Override
					public void run() {
						warper.msg("<red>Warp warm up cancelled.");
					}
				}
			);
		} else {
			this.player.teleport(fdata.warpLocations.get(warpName).getBukkitLocation());
			
			// Use getArg here to show original input
			msg("<gold>Taking you to <aqua>%s".replace("%s", getArg(0))); 
		}
	}
}
