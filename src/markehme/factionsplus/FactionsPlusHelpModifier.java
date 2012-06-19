package markehme.factionsplus;

import java.util.ArrayList;

import com.massivecraft.factions.cmd.FCommand;

public class FactionsPlusHelpModifier extends FCommand {
	public static ArrayList<ArrayList<String>> helpPages;
	
	public static void modify() {
				/*
				FactionsPlus.log.info("meow meow meow! {" + FactionsPlus.FactionsVersion + "}");
				helpPages = new ArrayList<ArrayList<String>>();
				ArrayList<String> pageLines;
				
				P.p.cmdBase.cmdHelp.updateHelp();
				
				pageLines = new ArrayList<String>();
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "FactionsPlus is no longer updating 1.6.x help pages"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f announce" + ChatColor.DARK_AQUA + " <message> " + ChatColor.YELLOW + "announces a message to your faction"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f ban" + ChatColor.DARK_AQUA + " <player> " + ChatColor.YELLOW + "kicks a player out of your faciton and stops them from re-joining"));
				P.p.cmdBase.cmdHelp.helpPages.add(pageLines);
				
				pageLines = new ArrayList<String>();
				pageLines.add(P.p.txt.parse("<i>Faction Warps"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "FactionsPlus is no longer updating 1.6.x help pages"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f warp" + ChatColor.DARK_AQUA + " <name> " + ChatColor.YELLOW + "warps to <name>"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f createwarp" + ChatColor.DARK_AQUA + " <name> " + ChatColor.YELLOW + "creates a warp called <name>"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f deletewarp" + ChatColor.DARK_AQUA + " <name> " + ChatColor.YELLOW + "removes the warp called <name>"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f listwarps" + ChatColor.DARK_AQUA + " " + ChatColor.YELLOW + "lists all the warps in your Faction"));
				
				P.p.cmdBase.cmdHelp.helpPages.add(pageLines);
				
				pageLines = new ArrayList<String>();
				pageLines.add(P.p.txt.parse("<i>Faction Jail:"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "FactionsPlus is no longer updating 1.6.x help pages"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f jail" + ChatColor.DARK_AQUA + " <player> " + ChatColor.YELLOW + "jails the specified player"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f unjail" + ChatColor.DARK_AQUA + " <player> " + ChatColor.YELLOW + "removes the specified player from Jail"));
				pageLines.add(P.p.txt.parse(ChatColor.AQUA + "/f setjail" + ChatColor.DARK_AQUA + " " + ChatColor.YELLOW + "sets the location of the Faction Jail"));
				
				P.p.cmdBase.cmdHelp.helpPages.add(pageLines);
				*/
	}	

	@Override
	public void perform() {
		return;
	}
}
