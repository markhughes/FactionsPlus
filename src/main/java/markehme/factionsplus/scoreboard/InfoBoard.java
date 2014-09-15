package markehme.factionsplus.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.scoreboard.Objective;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

/**
 * InfoBoard allows us to show territory-based information to
 * players
 *
 */
public class InfoBoard {

	private HashMap<UPlayer, InfoBoard> players = new HashMap<UPlayer, InfoBoard>();

	public InfoBoard get(UPlayer uplayer) {
		if(!players.containsKey(uplayer)) {
			players.put(uplayer, new InfoBoard(uplayer));
		}
		
		return players.get(uplayer);
	}
	
	// ------------------------ //
	
	private String objective_prefix = FactionsPlus.instance.scoreboardmanager.getPrefix() + "SMIP";
	
	public InfoBoard(UPlayer uplayer) {
		this.uplayer = uplayer;
	}
	
	private UPlayer uplayer = null;
	
	private boolean isShowing = false;
	private Faction lastKnownFaction = null;
	
	private Objective infoBoardObjective = null;
	
	public boolean isShowing() {
		return isShowing;
	}
	
	/**
	 * This method should only be called when a player changes chunks
	 */
	public void update(Faction from) {
		Faction f = BoardColls.get().getFactionAt(PS.valueOf(uplayer.getPlayer().getLocation()));
		
		if(f.isNone()) {
			infoBoardObjective = null;
			isShowing = false;
			
			uplayer.getPlayer().getScoreboard().getObjective(objective_prefix).unregister();
			
		} else {
			if(!f.getId().equals(from.getId())) {
				Rel rel = f.getRelationTo(from);
				
				List<String> lines = new ArrayList<String>();
				
				lines.add(Txt.parse(LConf.get().scoreboardInfoBoardTitle, f.getName()));
				
				if(rel.equals(Rel.ALLY)) {
					lines.add(Txt.parse(LConf.get().scoreboardIsYourAlly));
				} if(rel.equals(Rel.ENEMY)) {
					lines.add(Txt.parse(LConf.get().scoreboardIsYourEnemy));
				} if(rel.equals(Rel.NEUTRAL)) {
					lines.add(Txt.parse(LConf.get().scoreboardIsNeutral));
				} if(rel.equals(Rel.TRUCE)) {
					lines.add(Txt.parse(LConf.get().scoreboardIsInTruce));
				}
			}
		}
	}
	
	/**
	 * This is a modified version of a method by Barend Garvelink
	 * (https://github.com/barend)
	 * @param input
	 * @return List of strings 
	 */
	public List<String> breakString(String input) {
		StringTokenizer tok = new StringTokenizer(input, " ");
		StringBuilder output = new StringBuilder(input.length());
		
		int lineLen = 0;
		List<String> result = new ArrayList<String>();
		
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken();

			if (lineLen + word.length() > 15) {
				result.add(output + "-");
				output.setLength(0); // clear the buffer here
				lineLen = 0;
			}
			
			output.append(word);
			
			lineLen += word.length();
		}
		if(output.length() > 0) {
			result.add(output.toString());
		}
		
		output.setLength(0);
		
		return result;
	}

}

