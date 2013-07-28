package markehme.factionsplus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FPP;

import org.bukkit.ChatColor;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class FactionsPlusRules {	
	public static void removeRule(Faction faction, int ruleNo, UPlayer fplayer) {
		try {
			File readFile 	= 	new File(Config.folderFRules+File.separator+fplayer.getFactionId()+".rules");
			File outFile 	= 	new File(Config.folderFRules+File.separator+fplayer.getFactionId()+".new_rulesTMP");

			if(!readFile.exists()) {
				fplayer.msg(ChatColor.RED+"Your faction has no rules!");
				return;
			}
			
			if(Utilities.countLines(Config.folderFRules+File.separator+fplayer.getFactionId()+".rules") >= Config._rules.maxRulesPerFaction._) {
				fplayer.msg(ChatColor.RED+"The max rules a faction can have is "+Config._rules.maxRulesPerFaction._+ ".");
				return;
			}
			
			String workingLine = null;
			
			int i = 0;
			int c = 0;
			BufferedReader br = new BufferedReader(new FileReader(readFile));
			PrintWriter pw = new PrintWriter(new FileWriter(outFile));
			
			if(!outFile.exists()) { outFile.createNewFile(); }
			
			while ((workingLine = br.readLine()) != null) {
				i++;
				
				if(i != ruleNo) {
					c++;
					pw.println(workingLine);
					pw.flush();
				}
			}
			

			
			pw.close();
			br.close();
			
			if(c == 0) {
				fplayer.msg(ChatColor.RED+"Could not find that rule.");
				outFile.delete();
				return;
			} else if(i == c) {
				// 1 line was in the file, and 1 line was removed
				// which means there are no rules left. Remove the files. 
				readFile.delete();
				outFile.delete();
			} else {
				if (!readFile.delete()) {
					FPP.info("Could not remove the current rules file for Faction #" + faction.getId());
					fplayer.msg(ChatColor.RED+"Could not remove rule (internal error).");
					return;
				}
				
				if (!outFile.renameTo(readFile)) {
					FPP.info("Could not rename tmp file to current rules file for Faction #" + faction.getId());
					fplayer.msg(ChatColor.RED+"Could not remove rule (internal error).");
					return;
				}
			}
			
		} catch (FileNotFoundException ex) {
		  ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		fplayer.msg(ChatColor.GREEN+"Rule removed, rules re-shuffled.");
	}
	
	public static void sendRulesToPlayer(UPlayer fplayer) {
		
		File fRF = new File(Config.folderFRules+File.separator+fplayer.getFactionId()+".rules");
		
		if ( !fRF.exists() ) {
			fplayer.msg(ChatColor.WHITE+"No rules have been set for your Faction." );
			return;
		}
		
		fplayer.msg(ChatColor.GREEN+"Here are the rules for your Faction: " );
		
		FileInputStream fstream 	= 		null;
		DataInputStream in 			=		null;
		InputStreamReader isr 		=		null;
		BufferedReader br 			= 		null;
		
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
					fplayer.msg(ChatColor.AQUA+"Rule " + rCurrent + ": "+ChatColor.YELLOW + strLine );
				}
			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
			fplayer.msg( "Failed to show rules (Internal error -1021)" );
			return;
		} finally {
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
	
	public static void setRuleForFaction(Faction faction, UPlayer setter, String FPRule) {
				
		String[] argsa = new String[3];
		argsa[1] = setter.getName();
		argsa[2] = FPRule;

		File justAFactionsPlusRuleFile = new File(Config.folderFRules+File.separator+faction.getId()+".rules");
		
		if(faction.getId() == "0") {
			setter.msg(ChatColor.RED+"You must be in a Faction to set rules.");
			return;
		}
		
		if(!justAFactionsPlusRuleFile.exists()) {
			try {
				justAFactionsPlusRuleFile.createNewFile();
			} catch (IOException e) {
				setter.msg(ChatColor.RED+"Internal Error: Failed to create the FPRules file.");
				e.printStackTrace();
			}
		}
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.folderFRules+File.separator+faction.getId()+".rules", true)));
		    out.println(FPRule);
		    out.close();
		} catch (IOException e) {
			setter.msg(ChatColor.RED+"Internal Error: Failed to append the rule.");
		}
		
		setter.msg(ChatColor.GREEN+"Rule '"+FPRule+"' added!");
		
	}
}
