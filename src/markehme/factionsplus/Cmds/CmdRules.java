package markehme.factionsplus.Cmds;

import java.io.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

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
	
	@Override
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
		
		String[] argsa = new String[3];
		argsa[1] = sender.getName();
		argsa[2] = message;
		
		File fRF = new File( Config.folderFRules, fplayer.getFactionId() );
		if ( !fRF.exists() ) {
			fme.msg( "No rules have been set for your Faction." );
			return;
		}
		
		FileInputStream fstream=null;
		DataInputStream in=null;
		InputStreamReader isr = null;
		BufferedReader br=null;
		try {
			fstream = new FileInputStream( fRF );
			
			in = new DataInputStream( fstream );
			isr=new InputStreamReader( in );
			br = new BufferedReader( isr );
			
			String strLine;
			
			int rCurrent = 0;
			
			while ( ( strLine = br.readLine() ) != null ) {
				rCurrent = rCurrent + 1;
				
				if ( !strLine.isEmpty() || !strLine.trim().isEmpty() ) {
					fme.msg( "Rule " + rCurrent + ": " + strLine );
				}
			}
			
//			in.close();
		} catch ( Exception e ) {
			e.printStackTrace();
			sender.sendMessage( "Failed to show rules (Internal error -1021)" );
			return;
		}finally{
			if (null != br) {
				try {
					br.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			
			if (null != isr) {
				try {
					isr.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			
			if (null != in) {
				try {
					in.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			
			if (null != fstream) {
				try {
					fstream.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}

	}

}
