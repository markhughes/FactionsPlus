package me.markeh.factionsplus.migrator.fdata;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.types.TLoc;
import me.markeh.factionsplus.migrator.MigrationRunner;
import me.markeh.factionsplus.migrator.fdata.obj.OldFactionData;
import me.markeh.factionsplus.migrator.fdata.obj.OldFactionDataColls;

public class MigrateFData extends MigrationRunner {

	@Override
	public void run() {
		log("Starting migration of FactionData");
		
		for (Faction faction : Factions.get().getAll()) {
			OldFactionData oldFData = OldFactionDataColls.get().get2(faction.getID());
			
			FactionData newFData = FactionData.get(faction);
			
			if (oldFData == null || oldFData.detached()) continue;
			
			this.migrateWarps(oldFData, newFData);
			this.migrateRules(oldFData, newFData);
			
			newFData.save();
		}
	}
	
	public void migrateWarps(OldFactionData oldFData, FactionData newFData) {
		for (String warp : oldFData.getWarps().keySet()) newFData.warpLocations.add(warp.toLowerCase().trim(), TLoc.valueOf(oldFData.getWarpLocation(warp).asBukkitLocation()));
		
		for (String warp : oldFData.warpPasswords.keySet()) newFData.warpPasswords.add(warp.toLowerCase().trim(), oldFData.warpPasswords.get(warp));
	}
	
	public void migrateRules(OldFactionData oldFData, FactionData newFData) {
		for (String rule : oldFData.rules) newFData.rules.add(rule);
	}
	
	public void migrateJailLocation(OldFactionData oldFData, FactionData newFData) {
		newFData.jailLoc = TLoc.valueOf(oldFData.jailLocation.asBukkitLocation());
	}
	
	public void migrateJailedPlayers(OldFactionData oldFData, FactionData newFData) {
		for (String uuid : oldFData.jailedPlayerIDs.keySet()) newFData.jailedPlayers.add(uuid);
	}

}
