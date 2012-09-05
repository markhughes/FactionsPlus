package markehme.factionsplus.Cmds;

import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;



public class CmdReloadFP extends FPCommand {
	
	public CmdReloadFP() {
		super();
		this.aliases.add( "reloadfp" );
		
		this.optionalArgs.put( "all|conf|templates", "all");
		//XXX: only people that have factions.reload  permission can use /f reloadfp
		this.permission = Permission.RELOAD.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
		this.errorOnToManyArgs = true;
		
		this.setHelpShort( "Reloads FactionPlus config" );
		// TODO: maybe add optional params to also reload jails or what now, just in case they were edited manually
	}
	
	
	@SuppressWarnings( "boxing" )
	@Override
	public void performfp() {
		long startTime = System.nanoTime();
		String what = this.argAsString( 0, "all" ).toLowerCase();
		String fileWhat = null;
		boolean ret = false;
		try {
			if ( what.startsWith( "conf" ) ) {
				fileWhat=Config.fileConfig.getName();
				ret = Config.reloadConfig();
			} else
				if ( what.startsWith( "templ" ) ) {
					fileWhat=Config.templatesFile.getName();
					ret = Config.reloadTemplates();
				} else
					if ( what.equals( "all" ) ) {
						fileWhat=what;
						Config.reload();
						ret = true;//else it would just throw
					} else {
						msg( "<b>Invalid file specified. <i>Valid files: all, conf, templates" );
						return;
					}
		} catch ( Throwable t ) {
			t.printStackTrace();
			ret = false;
		} finally {
			long endTime = System.nanoTime() - startTime;
			if ( ret ) {
				msg( "<i>Reloaded FactionPlus <h>%s <i>from disk, took <h>%,8dns<i>.", fileWhat, endTime );
			} else {
				msg( ChatColor.RED+"Errors occurred while loading %s. See console for details.", fileWhat);
			}
		}
		
	}
	
}
