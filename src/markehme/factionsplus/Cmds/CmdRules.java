package markehme.factionsplus.Cmds;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TextUtil;

public class CmdRules extends FCommand {

	public CmdRules() {
		this.aliases.add("rules");
		this.errorOnToManyArgs = false;
		
		this.requiredArgs.add("number");
		this.requiredArgs.add("rule");
		//this.optionalArgs.put("on/off", "flip");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("view/manage Faction rules");
	}
	
	public void perform() {
		fme.msg("This is not yet ready.");
		
		String message = TextUtil.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
		
		if(!FactionsPlus.permission.has(sender, "factionsplus.viewrules")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		FPlayer fplayer = FPlayers.i.get(sender.getName());
		
		// TODO: Economy cost to view rules 
		// TODO: Make this function actually work
		
		String[] args = new String[3];
		args[1] = sender.getName();
		args[2] = message;
		
		try {
			if(!new File("plugins" + File.separator + "FactionsPlus" + File.separator + "frules" + File.separator + fplayer.getFactionId()).exists()) {
				fme.msg("No rules have been set for your Faction.");
				return;
			}
			
			try {
				FileInputStream fstream = new FileInputStream("plugins" + File.separator + "FactionsPlus" + File.separator + "frules" + File.separator + fplayer.getFactionId());
				
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				String strLine;
				
				int rCurrent = 0;
				
				while ((strLine = br.readLine()) != null)   {
					rCurrent = rCurrent + 1;
					
					if(!strLine.isEmpty() || strLine.trim() != "") {
						fme.msg("Rule " + rCurrent + ": " + strLine);
					}
				}
				
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		
			sender.sendMessage("Failed to show rules (Internal error -1021)");
			return;
		}


	}

}
