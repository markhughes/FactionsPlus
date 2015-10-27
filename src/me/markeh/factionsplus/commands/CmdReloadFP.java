package me.markeh.factionsplus.commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Texts;

public class CmdReloadFP extends FactionsCommand {
	
	// ----------------------------------------
	// Constructor
	// ----------------------------------------

	public CmdReloadFP() {
		this.aliases.add("reloadfp");
		
		this.requiredPermissions.add(Perm.get("factionsplus.reload", Texts.internalNoPermission));
		
		this.description = "Reload FactionsExtended";

	}
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------

	@Override
	public void run() {
		Plugin fe = Bukkit.getPluginManager().getPlugin("FactionsExtended");
		
		Bukkit.getPluginManager().disablePlugin(fe);
		Bukkit.getPluginManager().enablePlugin(fe);
		
		msg(Texts.pluginReloaded);
		
	}
}
