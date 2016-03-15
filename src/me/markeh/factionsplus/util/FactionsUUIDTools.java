package me.markeh.factionsplus.util;

import java.lang.reflect.Field;
import java.util.List;

import me.markeh.factionsframework.FactionsFramework;

import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCmdRoot;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.zcore.MCommand;

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
		return this.getSubcommands().add(fcommand);
	}
	
	public void removeCommand(FCommand fcommand) {
		this.getSubcommands().remove(fcommand);
	}
	
	public void addCommand(FCommand fcommand) {
		this.getSubcommands().add(fcommand);
	}
	
	@SuppressWarnings("unchecked")
	public List<MCommand<?>> getSubcommands() {
		try { 
			Class<? extends FCmdRoot> cmdBase = P.p.cmdBase.getClass();
			Field subCommands = cmdBase.getField("subCommands");
			return (List<MCommand<?>>) subCommands.get(P.p.cmdBase);
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
		return null;
	}
}
