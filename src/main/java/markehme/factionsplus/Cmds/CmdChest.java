package markehme.factionsplus.Cmds;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CmdChest extends FPCommand {

	public CmdChest() {
		this.aliases.add("chest");
		
		this.fpidentifier = "chest";
		
		this.optionalArgs.put("set", "looking");
		
		this.errorOnToManyArgs = true;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.CHEST.node));
		
		this.setHelp(LConf.get().cmdDescChest);
		this.setDesc(LConf.get().cmdDescChest);

	}
	@Override
	public void performfp() {
		if(args.size() > 0) {
			if(args.get(0).equalsIgnoreCase("set")) {
				if(me.hasPermission(FPPerm.CHEST.node)) {
					
					// This is deprecated, keep an eye on it .. 
					Block targetBlock = me.getTargetBlock(null, 100);
					
					if(!fpuconf.whoCanSetChest.get(usender.getRole())) {
						msg(Txt.parse(LConf.get().chestNotHighRank));
						return;
					}
					
					if(targetBlock.getType() == Material.CHEST) {
						fData.factionChest = PS.valueOf(targetBlock.getLocation());
					} else {
						msg(Txt.parse(LConf.get().chestNotLookingAt));
					}
				} else {
					msg(Txt.parse(LConf.get().chestNoPermission));
				}
				return;
			}
		}
		
		if(fData.factionChest == null) {
			msg(Txt.parse(LConf.get().chestNoneSet));
			return;
		}
		
		Location chestLoc = fData.factionChest.asBukkitLocation();
		Block chestBlock = chestLoc.getBlock();
		
		if(!BoardColls.get().getFactionAt(fData.factionChest).getId().equals(usender.getFactionId())) {
			msg(Txt.parse(LConf.get().chestNotInTerritory));
			return;
		}
		
		// TODO: add extra lockette, etc, checks? 
		
		if(chestBlock.getType() != Material.CHEST) {
			msg(Txt.parse(LConf.get().chestNotThere));
			fData.factionChest = null;
			return;
		}
		
		Chest chest = (Chest) chestBlock;
		
		me.openInventory(chest.getBlockInventory());
		
	}
}