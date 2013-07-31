package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.references.FP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdFactionNeed extends FPCommand {
	
	public CmdFactionNeed() { 
		this.aliases.add("need");
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.setHelp("announces to Faction Leaders, and Officers that you're in need of a Faction.");
		this.setDesc("announces to Faction Leaders, and Officers that you're in need of a Faction.");
	}
	
	@Override
	public void performfp() {
		if( ! FactionsPlus.permission.has( sender, "factionsplus.need" ) ) {
			
			sender.sendMessage( ChatColor.RED + "No permission!" );
			
			return;
			
		}
		
		if( usender.hasFaction() ) {
			msg( ChatColor.RED + "You already have a Faction!" );
			
			return;
		}
		
		// TODO: Move this into a thread
		// TODO: Cooldown? 
		// TODO: Allow certain Factions to ignore these messages ( /f needs ignore | /f needs listen )
		
		String[] argsa = { sender.getName() };
		
        for ( Player p : Bukkit.getServer().getOnlinePlayers() ) {
        	
        	if( UPlayer.get(p).getRole() == Rel.LEADER || UPlayer.get(p).getRole() == Rel.OFFICER ) {
        		
        		if( FP.permission.has( p, "factionsplus.ignoreneeds" ) ) {
        			
        			p.sendMessage( FactionsPlusTemplates.Go( "faction_need", argsa ).replace("!1", sender.getName() ) );
        			
        		}
        	}
        		

        }
	}
}
