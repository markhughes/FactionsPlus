package me.markeh.factionsframework.faction.versions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.Rel;
import me.markeh.factionsplus.FactionsPlus;

public class Faction1_6UUID extends Faction {
	com.massivecraft.factions.Faction faction = null;
	
	public Faction1_6UUID(String id) {
		super(id);
		
		if (this.faction != null) return;
		
		// As FactionsUUID has no getById we have to go through and find it 
		List<com.massivecraft.factions.Faction> factions = this.getAllFactions();// (id);
		
		for (com.massivecraft.factions.Faction checkFaction : factions) {
			if (checkFaction.getId() == id) {
				this.faction = checkFaction;
				return;
			}
		}
	}

	@Override
	public List<me.markeh.factionsframework.objs.FPlayer> getMembers() {
		List<me.markeh.factionsframework.objs.FPlayer> members = new ArrayList<me.markeh.factionsframework.objs.FPlayer>();
		
		for(FPlayer fPlayer : this.faction.getFPlayers()) {
			members.add(me.markeh.factionsframework.objs.FPlayer.get(fPlayer.getPlayer()));
		}
		
		return members;
	}

	@Override
	public me.markeh.factionsframework.objs.FPlayer getLeader() {
		if(this.faction == null) return null;
		if(this.faction.getId() == FactionsManager.get().fetch().getWildernessId()) return null;
		if(this.faction.getFPlayerAdmin() == null) return null;
		
		return me.markeh.factionsframework.objs.FPlayer.get(this.faction.getFPlayerAdmin().getPlayer());
	}

	@Override
	public List<me.markeh.factionsframework.objs.FPlayer> getOfficers() {
		List<me.markeh.factionsframework.objs.FPlayer> officers = new ArrayList<me.markeh.factionsframework.objs.FPlayer>();
		
		for(FPlayer fPlayer : this.faction.getFPlayers()) {
			if(fPlayer.getRole().equals(Role.MODERATOR)) {
				officers.add(me.markeh.factionsframework.objs.FPlayer.get(fPlayer.getPlayer()));
			}
		}
		
		return officers;
	}

	@Override
	public Location getHome() {
		return faction.getHome();
	}

	@Override
	public String getName() {
		return faction.getTag();
	}

	@Override
	public String getDescription() {
		return faction.getDescription();
	}

	@Override
	public List<Faction> getEnemies() {
		List<Faction> enemies = new ArrayList<Faction>();
		
		for(com.massivecraft.factions.Faction f : this.getAllFactions()) {
			if (this.faction.getRelationWish(f) == Relation.ENEMY) {
				enemies.add(me.markeh.factionsframework.faction.Factions.get().getFactionById(f.getId()));
			}
		}
		
		return enemies;
	}

	@Override
	public List<Faction> getAllies() {
		List<Faction> allies = new ArrayList<Faction>();
		
		for(com.massivecraft.factions.Faction f : this.getAllFactions()) {
			if (this.faction.getRelationWish(f) == Relation.ALLY) {
				allies.add(me.markeh.factionsframework.faction.Factions.get().getFactionById(f.getId()));
			}
		}
		
		return allies;	
	}

	@Override
	public Boolean isEnemyOf(Faction faction) {
		return this.getFactionById(faction.getID()).getRelationTo(this.faction) == Relation.ENEMY;
	}

	@Override
	public Boolean isAllyOf(Faction faction) {
		return this.getFactionById(faction.getID()).getRelationTo(this.faction) == Relation.ALLY;
	}

	@Override
	public int getLandCount() {
		return faction.getAllClaims().size();
	}

	@Override
	public Rel getRealtionshipTo(Object compare) {
		Relation theRelationship = null;
		
		if (compare instanceof Player) {
			compare = me.markeh.factionsframework.objs.FPlayer.get((Player) compare);
		}
		
		if (compare instanceof Faction) {
			com.massivecraft.factions.Faction otherFaction = this.getFactionById(((Faction) compare).getID());
			theRelationship = otherFaction.getRelationTo(faction);			
		} 
		
		if (compare instanceof FPlayer) {
			me.markeh.factionsframework.objs.FPlayer fplayer = (me.markeh.factionsframework.objs.FPlayer) compare;
			
			if (fplayer.isLeader() && fplayer.getFactionID() == this.getID()) return Rel.LEADER;
			if (fplayer.isOfficer() && this.getOfficers().contains(fplayer.getPlayer())) return Rel.OFFICER;
			
			theRelationship = this.faction.getRelationTo(FPlayers.getInstance().getByPlayer(fplayer.getPlayer()));
		}
		
		if (theRelationship == Relation.ALLY) return Rel.ALLY;
		if (theRelationship == Relation.ENEMY) return Rel.ENEMY;
		if (theRelationship == Relation.NEUTRAL) return Rel.NEUTRAL;
		if (theRelationship == Relation.TRUCE) return Rel.TRUCE;
		if (theRelationship == Relation.MEMBER) return Rel.MEMBER;
		
		return null;
	}

	@Override
	public boolean isWilderness() {
		return (this.faction.isWilderness());
	}

	@Override
	public double getPower() {
		return this.faction.getPower();
	}
	
	public com.massivecraft.factions.Factions getInstance() {
		try {
			return (Factions) Factions.class.getMethod("getInstance").invoke(this);
		} catch (Exception e) {
			FactionsPlus.get().logError(e);
			return null;
		}
	}
	
	public com.massivecraft.factions.Faction getFactionById(String id) {
		try {
			return (com.massivecraft.factions.Faction) this.getInstance().getClass().getMethod("getFactionById", String.class).invoke(this.getInstance(), id);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			FactionsFramework.get().logError(e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<com.massivecraft.factions.Faction> getAllFactions() {
		try {
			return (List<com.massivecraft.factions.Faction>) this.getInstance().getClass().getMethod("getAllFactions").invoke(this.getInstance());
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
			return null;
		}
	}
}
