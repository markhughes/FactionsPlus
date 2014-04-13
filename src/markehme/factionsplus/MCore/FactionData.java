package markehme.factionsplus.MCore;

import java.util.Map;

import com.massivecraft.factions.entity.Faction;
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
	
	// --
	
	public boolean warpExists(String name) {
		if(warpLocation.get(name.toLowerCase()) == null) {
			return false;
		}
		
		return true;
	}
	
	public boolean warpHasPassword(String name) {
		return (warpPasswords.get(name.toLowerCase()) != null);
	}
	
	public boolean warpValidatePassword(String name, String pass) {
		if(!warpHasPassword(name.toLowerCase())) {
			return true;
		}
		
		if(warpPasswords.get(name.toLowerCase()) == pass) {
			return true;
		}
		
		return false;
	}
	
	public PS getWarpLocation(String warp) {
		return(warpLocation.get(warp.toLowerCase()));
	}
	
	public Faction faction = null; 
	
	public Map<String, PS> warpLocation;
	public Map<String, String> warpPasswords;
	
	public PS k = null;

}
