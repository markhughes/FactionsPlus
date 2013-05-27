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

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TextUtil;

public class CmdRules extends FPCommand {

	
	public CmdRules() {
		this.aliases.add("rules");
		
		this.errorOnToManyArgs = false;
		
		this.permission = Permission.HELP.node;
		
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("view Faction rules");
		
	}
	
	@Override
	public void performfp() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.viewrules")) {
			fme.msg(ChatColor.RED + "No permission!");
			return;
		}
		
		FactionsPlusRules.sendRulesToPlayer(fme);

	}

}
