package me.markeh.factionsframework.faction.versions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Rel;

public class Faction2X extends Faction {
	
	private com.massivecraft.factions.entity.Faction faction = null;
	
	public Faction2X(String uuid) {
		super(uuid);
		
		if (this.faction != null) return;
		
		this.faction = FactionColl.get().get(uuid);
	}

	@Override
	public List<Player> getMembers() {
		List<Player> members = new ArrayList<Player>();
		
		for (MPlayer mplayer : this.faction.getMPlayers()) {
			members.add(mplayer.getPlayer());
		}
		
		return members;
	}

	@Override
	public Player getLeader() {
		return this.faction.getLeader().getPlayer();
	}

	@Override
	public List<Player> getOfficers() {
		List<Player> members = new ArrayList<Player>();
		
		for (MPlayer mplayer : this.faction.getMPlayers()) {
			if (mplayer.getRole() == com.massivecraft.factions.Rel.OFFICER) {
				members.add(mplayer.getPlayer());
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
		return this.faction.getRelationWish(FactionColl.get().get(faction.getID())) == com.massivecraft.factions.Rel.ENEMY;
	}

	@Override
	public Boolean isAllyOf(Faction faction) {
		return this.faction.getRelationWish(FactionColl.get().get(faction.getID())) == com.massivecraft.factions.Rel.ALLY;
	}

	@Override
	public int getLandCount() {
		return BoardColl.get().getCount(FactionColl.get().get(UUID.fromString(this.faction.getId())));
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
}
