package me.markeh.factionsframework.faction.versions;

import java.util.ArrayList;
import java.util.List;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Rel;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.UPlayer;

public class Faction2_6 extends Faction {
	
	private com.massivecraft.factions.entity.Faction faction = null;
	
	public Faction2_6(String uuid) {
		super(uuid);
		
		if (this.faction != null) return;
		
		for (FactionColl fc : FactionColls.get().getColls()) {
			for(com.massivecraft.factions.entity.Faction f : fc.getAll()) {
				if (f.getId().equalsIgnoreCase(uuid)) this.faction = f;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> getMembers() {
		List<Player> members = new ArrayList<Player>();
		
		try {
			// Requires reflection - getUPlayers is not in latest Faction class, so the ref is broken
			for (UPlayer mplayer : (List<UPlayer>) this.faction.getClass().getMethod("getUPlayers").invoke(this)) {
				members.add(mplayer.getPlayer());
			}
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
		
		return members;
	}

	@Override
	public Player getLeader() {
		return this.faction.getLeader().getPlayer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> getOfficers() {
		List<Player> members = new ArrayList<Player>();
		
		try {
			// Requires reflection - getUPlayers is not in latest Faction class, so the ref is broken
			for (UPlayer mplayer : (List<UPlayer>) this.faction.getClass().getMethod("getUPlayers").invoke(this)) {
				if (mplayer.getRole() == com.massivecraft.factions.Rel.OFFICER) {
					members.add(mplayer.getPlayer());
				}
			}
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
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
		return this.getRealtionshipTo(faction) == Rel.ENEMY;
	}

	@Override
	public Boolean isAllyOf(Faction faction) {
		return this.getRealtionshipTo(faction) == Rel.ALLY;
	}

	@Override
	public int getLandCount() {
		return BoardColls.get().getChunks(this.faction).size();
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
			compare = FactionColls.get().get2(compare);
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
