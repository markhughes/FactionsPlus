package me.markeh.factionsframework.faction.versions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.Rel;

public class Faction16UUID extends Faction {
	com.massivecraft.factions.Faction faction = null;
	
	public Faction16UUID(String id) {
		super(id);
		
		if (this.faction != null) return;
		
		// As FactionsUUID has no getById we have to go through and find it 
		List<com.massivecraft.factions.Faction> factions = com.massivecraft.factions.Factions.getInstance().getAllFactions();// (id);
		
		for (com.massivecraft.factions.Faction checkFaction : factions) {
			if (checkFaction.getId() == id) {
				this.faction = checkFaction;
				return;
			}
		}
	}

	@Override
	public List<Player> getMembers() {
		List<Player> members = new ArrayList<Player>();
		
		for(FPlayer fPlayer : this.faction.getFPlayers()) {
			members.add(fPlayer.getPlayer());
		}
		
		return members;
	}

	@Override
	public Player getLeader() {
		if(this.faction == null) return null;
		if(this.faction.getId() == FactionsManager.get().fetch().getWildernessID()) return null;
		if(this.faction.getFPlayerAdmin() == null) return null;
		
		return this.faction.getFPlayerAdmin().getPlayer();
	}

	@Override
	public List<Player> getOfficers() {
		List<Player> officers = new ArrayList<Player>();
		
		for(FPlayer fPlayer : this.faction.getFPlayers()) {
			if(fPlayer.getRole().equals(Role.MODERATOR)) {
				officers.add(fPlayer.getPlayer());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Faction> getAllies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isEnemyOf(Faction faction) {
		return Factions.getInstance().getFactionById(faction.getID()).getRelationTo(this.faction) == Relation.ENEMY;
	}

	@Override
	public Boolean isAllyOf(Faction faction) {
		return Factions.getInstance().getFactionById(faction.getID()).getRelationTo(this.faction) == Relation.ALLY;
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
			com.massivecraft.factions.Faction otherFaction = Factions.getInstance().getFactionById(((Faction) compare).getID());
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
}
