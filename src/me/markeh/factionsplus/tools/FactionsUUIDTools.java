package me.markeh.factionsplus.tools;

import com.massivecraft.factions.P;

public class FactionsUUIDTools {
	private static FactionsUUIDTools i = null;
	public static FactionsUUIDTools get() { 
		if (i == null) i = new FactionsUUIDTools();
		
		return i;
	}
	
	// Removes FactionsUUID warp commands 
	public void removeFactionsUUIDWarpCommands() {
		if(P.p.cmdBase.subCommands.contains(P.p.cmdBase.cmdFWarp)) {
			P.p.cmdBase.subCommands.remove(P.p.cmdBase.cmdFWarp);
		}
		
		if(P.p.cmdBase.subCommands.contains(P.p.cmdBase.cmdDelFWarp)) {
			P.p.cmdBase.subCommands.remove(P.p.cmdBase.cmdDelFWarp);
		}
		
		if(P.p.cmdBase.subCommands.contains(P.p.cmdBase.cmdSetFWarp)) {
			P.p.cmdBase.subCommands.remove(P.p.cmdBase.cmdSetFWarp);
		}
	}

	public void addFactionsUUIDWarpCommands() {
		if ( ! P.p.cmdBase.subCommands.contains(P.p.cmdBase.cmdFWarp)) {
			P.p.cmdBase.subCommands.add(P.p.cmdBase.cmdFWarp);
		}
		
		if ( ! P.p.cmdBase.subCommands.contains(P.p.cmdBase.cmdDelFWarp)) {
			P.p.cmdBase.subCommands.add(P.p.cmdBase.cmdDelFWarp);
		}
		
		if ( ! P.p.cmdBase.subCommands.contains(P.p.cmdBase.cmdSetFWarp)) {
			P.p.cmdBase.subCommands.add(P.p.cmdBase.cmdSetFWarp);
		}	
	}
}
