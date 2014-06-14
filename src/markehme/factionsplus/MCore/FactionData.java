package markehme.factionsplus.MCore;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.Entity;

public class FactionData extends Entity<FactionData> {
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	public static FactionData get(Object oid)
	{
		return FactionDataColls.get().get2(oid);
	}

	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public FactionData load(FactionData that) {
		this.faction = that.faction;

		return this;
	}

	@Override
	public void preDetach(String id) {
		
		// Not sure we need to use this yet.
		
		//String universe = this.getUniverse();


	}
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	/**
	 * Check if a warp exists. 
	 * @param name
	 * @return
	 */
	public boolean warpExists(String name) {
		if(warpLocation.get(name.toLowerCase()) == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if a warp has a password. Returns true if a warp has a password. 
	 * @param name
	 * @return
	 */
	public boolean warpHasPassword(String name) {
		return (warpPasswords.get(name.toLowerCase()) != null);
	}
	
	/**
	 * Validate the password of a warp, returns true if it is valid. 
	 * @param name
	 * @param pass
	 * @return
	 */
	public boolean warpValidatePassword(String name, String pass) {
		if(!warpHasPassword(name.toLowerCase())) {
			return true;
		}
		
		if(warpPasswords.get(name.toLowerCase()) == pass) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the (PS) Location of a warp 
	 * @param warp
	 * @return
	 */
	public PS getWarpLocation(String warp) {
		return(warpLocation.get(warp.toLowerCase()));
	}
	
	/**
	 * Returns a map of the warps and their locations 
	 * @return
	 */
	public Map<String, PS> getWarps() {
		return null;
	}
	
	public boolean isJailed(UPlayer uPlayer) {
		return(isJailed(uPlayer.getPlayer()));
	}
	
	public boolean isJailed(Player player) {
		return(this.jailedPlayerIDs.containsKey(player.getUniqueId().toString()));
	}
	
	// -------------------------------------------- //
	// VARIABLES
	// -------------------------------------------- //
	
	public Faction faction = null; 
	
	public Map<String, PS> warpLocation;
	public Map<String, String> warpPasswords;
	
	public String announcement = null;
	
	public Map<String, String> bannedPlayerIDs;	
	
	public Map<String, PS> jailedPlayerIDs;	
	public PS jailLocation = null;
	
	public List<String> rules;
	
	
}
