package me.markeh.factionsplus.util;

import me.markeh.factionsframework.FactionsFramework;

import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.massivecore.cmd.MassiveCommand;

public class FactionsUUIDTools {
	private static FactionsUUIDTools i = null;
	public static FactionsUUIDTools get() { 
		if (i == null) i = new FactionsUUIDTools();
		
		return i;
	}
	
	// Removes FactionsUUID warp commands 
	public void removeFactionsUUIDWarpCommands() {
		if( this.containsCommand(P.p.cmdBase.cmdFWarp)) {
			this.removeCommand(P.p.cmdBase.cmdFWarp);
		}
		
		if( this.containsCommand(P.p.cmdBase.cmdDelFWarp)) {
			this.removeCommand(P.p.cmdBase.cmdDelFWarp);
		}
		
		if( this.containsCommand(P.p.cmdBase.cmdSetFWarp)) {
			this.removeCommand(P.p.cmdBase.cmdSetFWarp);
		}
	}

	public void addFactionsUUIDWarpCommands() {
		if ( ! this.containsCommand(P.p.cmdBase.cmdFWarp)) {
			this.addCommand(P.p.cmdBase.cmdFWarp);
		}
		
		if ( ! this.containsCommand(P.p.cmdBase.cmdDelFWarp)) {
			this.addCommand(P.p.cmdBase.cmdDelFWarp);
		}
		
		if ( ! this.containsCommand(P.p.cmdBase.cmdSetFWarp)) {
			this.addCommand(P.p.cmdBase.cmdSetFWarp);
		}	
	}
	
	public boolean containsCommand(FCommand fcommand) {
		try {
			Boolean result = (Boolean) P.p.cmdBase.getClass().getField("subCommands").getClass().getMethod("contains", MassiveCommand.class).invoke(this, fcommand);
			return result;
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
			
			return false;
		}
	}
	
	public void removeCommand(FCommand fcommand) {
		try {
			P.p.cmdBase.getClass().getField("subCommands").getClass().getMethod("remove", MassiveCommand.class).invoke(this, fcommand);
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
	}
	
	public void addCommand(FCommand fcommand) {
		try {
			P.p.cmdBase.getClass().getField("subCommands").getClass().getMethod("add", MassiveCommand.class).invoke(this, fcommand);
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
	}
}
