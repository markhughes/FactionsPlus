package me.markeh.factionsframework.faction;

import java.util.List;

import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Rel;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Faction {
	
	private final String id;
	public Faction(String id) {
		this.id = id;
	}
	
	// Returns the ID of the Faction
	public String getID() { return id; }
	
	// Returns a list of players in the Faction
	public abstract List<Player> getMembers();
	
	// Gets the leader of the Faction
	public abstract Player getLeader();
	
	// Returns a list of the officers in a Faction
	public abstract List<Player> getOfficers();
	
	// Gets the Home location of the Faction
	public abstract Location getHome();
	
	// Gets the name of the Faction
	public abstract String getName();
	
	// Gets the description of the Faction
	public abstract String getDescription();
	
	// Returns a list of enemies
	public abstract List<Faction> getEnemies();
	
	// Returns a list of allies
	public abstract List<Faction> getAllies();
	
	// Determines if a Faction is an enemy 
	public abstract Boolean isEnemyOf(Faction faction);
	
	// Determines if a Faction is an ally 
	public abstract Boolean isAllyOf(Faction faction);
		
	// Returns how much the Faction has claimed 
	public abstract int getLandCount();
	
	public abstract Rel getRealtionshipTo(Object compare);

	public abstract boolean isWilderness();
	
	public abstract double getPower();

	public void msg(String string) {
		for(Player player : this.getMembers()) {
			FPlayer.get(player).msg(string);
		}
	}
	
	public void msg(String string, String... params) {
		for(Player player : this.getMembers()) {
			FPlayer.get(player).msg(string, params);
		}
	}
	
}
