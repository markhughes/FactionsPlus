package me.markeh.factionsplus.migrator.fdata;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.types.TLoc;
import me.markeh.factionsplus.migrator.MigrationRunner;
import me.markeh.factionsplus.migrator.fdata.obj.FactionDataColls;

public class MigrateFData extends MigrationRunner {

	@Override
	public void run() {
		log("Starting migration of FactionData");
		
		for (Faction faction : Factions.get().getAll()) {
			me.markeh.factionsplus.migrator.fdata.obj.FactionData oldFData = FactionDataColls.get().get2(faction.getID());
			
			FactionData newFData = FactionData.get(faction);
			
			if (oldFData == null || oldFData.detached()) continue;
			
			this.migrateWarps(oldFData, newFData);
			this.migrateRules(oldFData, newFData);
			
			newFData.save();
		}
	}
	
	public void migrateWarps(me.markeh.factionsplus.migrator.fdata.obj.FactionData oldFData, FactionData newFData) {
		for (String warp : oldFData.getWarps().keySet()) newFData.warpLocations.add(warp.toLowerCase().trim(), TLoc.valueOf(oldFData.getWarpLocation(warp).asBukkitLocation()));
		
		for (String warp : oldFData.warpPasswords.keySet()) newFData.warpPasswords.add(warp.toLowerCase().trim(), oldFData.warpPasswords.get(warp));
	}
	
	public void migrateRules(me.markeh.factionsplus.migrator.fdata.obj.FactionData oldFData, FactionData newFData) {
		for (String rule : oldFData.rules) newFData.rules.add(rule);
	}

}
