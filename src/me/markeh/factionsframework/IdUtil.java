package me.markeh.factionsframework;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class IdUtil {

	private static HashMap<String, String> idMap = new HashMap<String, String>();
	private static Boolean attemptedLoad = false;
	
	public static String get(Player player) {
		load();
		if (player.getUniqueId() != null) return player.getUniqueId().toString();
		
		return getFromMap(player);
	}
	

	public static void mapLoad(HashMap<String, String> map) {
		idMap = map;
	}
	
	public static HashMap<String, String> mapGet() {
		return idMap;
	}
	
	private static String getFromMap(Player player) {
		String playerName = player.getName().toLowerCase();
		
		if ( ! idMap.containsKey(playerName)) {
			idMap.put(playerName, UUID.randomUUID().toString());
			save();
		}
		return idMap.get(playerName);
	}
	
	private static void load() {
		if ( ! attemptedLoad) return;
		
	}
	
	private static void save() {
		attemptedLoad = true;
		
	}
}
