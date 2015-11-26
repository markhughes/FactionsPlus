package me.markeh.factionsplus.scoreboard.compatibility;

import mc.alk.arena.BattleArena;

import org.bukkit.entity.Player;

public class CompatibilityBattleArena extends SBCompatibility {

	// Disable scoreboard if they're in a BattleArea game
	@Override
	public boolean showForPlayer(Player player) {
		return ! BattleArena.inArena(player);
	}
}
