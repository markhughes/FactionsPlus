package me.markeh.factionsframework.faction.versions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Rel;

public class Faction2_X extends Faction {
	
	private com.massivecraft.factions.entity.Faction faction = null;
	
	public Faction2_X(String uuid) {
		super(uuid);
		
		if (this.faction != null) return;
		
		this.faction = FactionColl.get().get(uuid);
	}

	@Override
	public List<FPlayer> getMembers() {
		List<FPlayer> members = new ArrayList<FPlayer>();
		
		for (MPlayer mplayer : this.faction.getMPlayers()) {
			members.add(FPlayer.get(mplayer.getPlayer()));
		}
		
		return members;
	}

	@Override
	public FPlayer getLeader() {
		if (this.faction.getLeader() == null) return null;
		if (this.faction.getLeader().getPlayer() == null) return null;
		
		return FPlayer.get(this.faction.getLeader().getPlayer());
	}

	@Override
	public List<FPlayer> getOfficers() {
		List<FPlayer> members = new ArrayList<FPlayer>();
		
		for (MPlayer mplayer : this.faction.getMPlayers()) {
			if (mplayer.getRole() == com.massivecraft.factions.Rel.OFFICER) {
				members.add(FPlayer.get(mplayer.getPlayer()));
			}
		}
		
		return members;
	}

	@Override
	public Location getHome() {
		return this.faction.getHome().asBukkitLocation();
	}

	@Override
	public String getName() {
		return this.faction.getName();
	}

	@Override
	public String getDescription() {
		return this.faction.getDescription();
	}

	@Override
	public List<Faction> getEnemies() {
		List<Faction> enemies = new ArrayList<Faction>();
		
		for (String factionid : faction.getRelationWishes().keySet()) {
			if (faction.getRelationWishes().get(factionid) == com.massivecraft.factions.Rel.ENEMY) {
				enemies.add(Factions.get().getFactionById(factionid));
			}
		}
		
		return enemies;
	}

	@Override
	public List<Faction> getAllies() {
		List<Faction> allies = new ArrayList<Faction>();
		
		for (String factionid : faction.getRelationWishes().keySet()) {
			if (faction.getRelationWishes().get(factionid) == com.massivecraft.factions.Rel.ALLY) {
				allies.add(Factions.get().getFactionById(factionid));
			}
		}
		
		return allies;
	}

	@Override
	public Boolean isEnemyOf(Faction faction) {
		return this.faction.getRelationWish(FactionColl.get().get(faction.getID())) == com.massivecraft.factions.Rel.ENEMY;
	}

	@Override
	public Boolean isAllyOf(Faction faction) {
		return this.faction.getRelationWish(FactionColl.get().get(faction.getID())) == com.massivecraft.factions.Rel.ALLY;
	}

	@Override
	public int getLandCount() {
		return BoardColl.get().getCount(this.faction);
	}

	@Override
	public Rel getRealtionshipTo(Object compare) {
		if (compare instanceof Player) {
			compare = MPlayer.get(compare);
		}
		
		if (compare instanceof FPlayer) {
			compare = MPlayer.get(((FPlayer) compare).getPlayer());
		}
		
		if (compare instanceof Faction) {
			compare = FactionColl.get().get(((Faction) compare).getID());
		}
		
		com.massivecraft.factions.Rel rel = this.faction.getRelationTo((RelationParticipator) compare);
		
		if (rel == com.massivecraft.factions.Rel.ALLY) return Rel.ALLY;
		if (rel == com.massivecraft.factions.Rel.ENEMY) return Rel.ENEMY;
		if (rel == com.massivecraft.factions.Rel.LEADER) return Rel.LEADER;
		if (rel == com.massivecraft.factions.Rel.MEMBER) return Rel.MEMBER;
		if (rel == com.massivecraft.factions.Rel.NEUTRAL) return Rel.NEUTRAL;
		if (rel == com.massivecraft.factions.Rel.OFFICER) return Rel.OFFICER;
		if (rel == com.massivecraft.factions.Rel.RECRUIT) return Rel.RECRUIT;
		if (rel == com.massivecraft.factions.Rel.TRUCE) return Rel.TRUCE;
		
		return null;
	}
	
	@Override
	public boolean isWilderness() {
		return (this.faction.isNone());
	}
	
	@Override
	public double getPower() {
		return this.faction.getPower();
	}
	
}
