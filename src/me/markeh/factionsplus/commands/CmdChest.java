package me.markeh.factionsplus.commands;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.types.TLoc;

public class CmdChest  extends FactionsCommand {

	// ----------------------------------------
	// Constructor
	// ----------------------------------------

	public CmdChest() {
		this.aliases.add("chest");
		this.optionalArguments.put("set", "no");
		
		this.requiredPermissions.add(Perm.get("factionsplus.chests", "You don't have permission to uses faction chests."));
		
		this.description = "Set and use a chest for your faction";
		
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
	}
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------
	
	@Override
	public void run() {
		FactionData fdata = FactionData.get(faction.getID());
				
		if (this.getArg(0) != null) {
			if (this.getArg(0).equalsIgnoreCase("set")) {
				if ( ! fplayer.isLeader() && !fplayer.isOfficer()) {
					msg("Your rank is not high enough to set the faction chest");
					return;
				}
				
				Block targetBlock = this.player.getTargetBlock((Set<Material>) null, 50);
				
				if (targetBlock.getType() == Material.CHEST) {
					fdata.factionChest = new TLoc(targetBlock.getLocation());
					msg("<green>Faction chest set!");
				} else {
					msg("<red>You must be looking at a chest to set the location.");
				}
				
				return;
			}
		}
		
		if (fdata.factionChest == null) {
			msg("No faction chest has been set");
			return;
		}
		
		Faction faction = factions.getFactionAt(fdata.factionChest.getBukkitLocation());
		
		if (faction.getID() != faction.getID()) {
			msg("The Faction chest is not in your Faction land");
			return;
		}
		
		if (fdata.factionChest.getBlock().getType() != Material.CHEST) {
			msg("The faction chest can no longer be accessed.");
			return;
		}
		
		if (fdata.factionChest.getBlock() instanceof DoubleChest) {
			DoubleChest chest = (DoubleChest) fdata.factionChest.getBlock().getState();
			player.openInventory(chest.getInventory());
		} else {
			Chest chest = (Chest) fdata.factionChest.getBlock().getState();
			player.openInventory(chest.getInventory());
		}
		
	}
}
