package markehme.factionsplus.Cmds;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusRules;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdRules extends FPCommand {

	
	public CmdRules() {
		
		this.aliases.add( "rules" );
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "view Faction rules" );
		this.setDesc( "view Faction rules" );
		
	}
	
	@Override
	public void performfp() {
		if( ! FactionsPlus.permission.has( sender, "factionsplus.viewrules" ) ) {
			msg( ChatColor.RED + "No permission!" );
			return;
		}
		
		FactionsPlusRules.sendRulesToPlayer(usender);

	}

}
