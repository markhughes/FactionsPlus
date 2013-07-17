package markehme.factionsplus.Cmds;

import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;



public class CmdReloadFP extends FPCommand {
	
	public CmdReloadFP() {
		super();
		this.aliases.add( "reloadfp" );
		
		//this.optionalArgs.put( "all|conf|templates", "all");
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		
		this.setHelp( "reloads FactionPlus config" );
		this.setDesc( "reloads FactionPlus config" );
	}
	
	
	@SuppressWarnings( "boxing" )
	@Override
	public void performfp() {
		long startTime = System.nanoTime();
		boolean ret = false;
		try {
			// Templates have been moved into the config
			// (in the future, templates should work off the same system as config
			//  but in a seperate file)
			
			/*
			if ( what.startsWith( "conf" ) ) {
				fileWhat=Config.fileConfig.getName();
				ret = Config.reloadConfig();
			} else
				if ( what.startsWith( "templ" ) ) {
					fileWhat=Config.templatesFile.getName();
					ret = Config.reloadTemplates();
				} else
					if ( what.equals( "all" ) ) {*/
						
						Config.reload();
						ret = true; // otherwise it would just throw
						
					/*} else {
						msg( "<b>Invalid file specified. <i>Valid files: all, conf, templates" );
						return;
					}*/
		} catch ( Throwable t ) {
			t.printStackTrace();
			ret = false;
		} finally {
			long endTime = System.nanoTime() - startTime;
			if ( ret ) {
				msg( "<i>Reloaded FactionPlus <i>from disk, took <h>%,2dms<i>.", endTime / 1000000 );//ns to ms
			} else {
				msg( ChatColor.RED+"Errors occurred while reloading. See console for details." );
			}
		}
		
	}
	
}
