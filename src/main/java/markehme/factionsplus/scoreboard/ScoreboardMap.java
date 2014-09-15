package markehme.factionsplus.scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import markehme.factionsplus.extras.FType;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class ScoreboardMap {
	
	private HashMap<UPlayer, ScoreboardMap> players = new HashMap<UPlayer, ScoreboardMap>();
	
	public ScoreboardMap get(UPlayer uplayer) {
		if(!players.containsKey(uplayer)) {
			players.put(uplayer, new ScoreboardMap(uplayer));
		}
		
		return players.get(uplayer);
	}
	
	// ---------------
	
	private UPlayer uplayer;
	private ChatColor colorPermanent = ChatColor.DARK_AQUA;
	private ChatColor colorWarZone = ChatColor.DARK_RED;
	
	private ChatColor colorWilderness = ChatColor.DARK_GREEN;
	
	private ChatColor colorFactionAlly = ChatColor.LIGHT_PURPLE;
	private ChatColor colorFactionNeutral = ChatColor.GRAY;
	private ChatColor colorFactionEnemy = ChatColor.RED;

	public ScoreboardMap(UPlayer uplayer) {
		this.uplayer = uplayer;
		
	}
	
	public String allocate(Faction f) {
		if(f.getFlag(FFlag.PERMANENT)) {
			if(FType.valueOf(f).equals(FType.WARZONE)) {
				return colorWarZone + "▓";
			}
			
			return colorPermanent + "▓";
		}
		
		if(f.getRelationTo(uplayer.getFaction()).equals(Rel.ALLY)) {
			return colorFactionAlly + "▓";
		}
		
		if(f.getRelationTo(uplayer.getFaction()).equals(Rel.NEUTRAL) || f.getRelationTo(uplayer.getFaction()).equals(Rel.TRUCE)) {
			return colorFactionNeutral + "▓";
		}
		
		if(f.getRelationTo(uplayer.getFaction()).equals(Rel.ENEMY)) {
			return colorFactionEnemy + "▓";
		}
		
		
		return colorWilderness + "▓";
	}
	
	/*
	 ▓###▓▓▓
	 ▓▓▓▓▓▓▓
	 ▓▒▓▓▓▓▓
	 ▓▒▒▒▒▓▓
	 ▓▒▒▓▓▓▓
	 ▓▒▒▒▓▓▓
	 ▓▓▓▓▓▓▓
	 */
	
	private String wilderness	= "▓";
	private String faction		= "▒";
	private String permanent	= "#";
	
	/**
	 * Updates the map to the players position
	 */
	public void update() {
		return;
	}
	
	public List<String> get() {
		
		return null;
	}
}
