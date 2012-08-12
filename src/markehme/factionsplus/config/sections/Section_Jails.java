package markehme.factionsplus.config.sections;

import com.massivecraft.factions.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;


public final class Section_Jails {
	
	@Option(oldAliases_alwaysDotted={
		"jails.enableJails"
		,"enableJails"
		}, realAlias_inNonDottedFormat = "enabled" )
	public  final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"jails.leadersCanSetJails"
		"leadersCanSetJails"
		}, realAlias_inNonDottedFormat = "leadersCanSetJails" )
	public  final _boolean leadersCanSetJails=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanSetJails"
		}, realAlias_inNonDottedFormat = "officersCanSetJails" )
	public  final _boolean officersCanSetJails=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanSetJails"
		}, realAlias_inNonDottedFormat = "membersCanSetJails" )
	public  final _boolean membersCanSetJails=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanJail"
		}, realAlias_inNonDottedFormat = "leadersCanJail" )
	public  final _boolean leadersCanJail=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanJail"
		}, realAlias_inNonDottedFormat = "officersCanJail" )
	public  final _boolean officersCanJail=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		}, realAlias_inNonDottedFormat = "denyMovementWhileJailed" )
	public  final _boolean denyMovementWhileJailed=new _boolean(true);
	
	@Option(
		autoComment={"if true, it'll require leaders/officers to also have "+permissionNodeNameForCanJailUnjail+" permission node"
		,"before they will be allowed to jail/unjail"	
		},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "furtherRestrictJailUnjailToThoseThatHavePermission" )
	public  final _boolean furtherRestrictJailUnjailToThoseThatHavePermission=new _boolean(false);
	
	
	@Option(
		autoComment={"if true, it'll require the player that issues /f jail to be located inside his own faction territory"
		," before they will be allowed to jail (unjailing will work regardless)"
		,"if false, /f jail can be used from any location"
		,"setting this to true will prevent two+ players for abusing /f jail",
		" by issuing it on each other when they are in danger"
		},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "canJailOnlyIfIssuerIsInOwnTerritory" )
	public  final _boolean canJailOnlyIfIssuerIsInOwnTerritory=new _boolean(true);
	
	
	public final static String permissionNodeNameForCanJailUnjail="factionsplus.jailunjail";
	
	public final static boolean canJailUnjail(FPlayer whoCan) {
		return ( 
				( Utilities.isOp( whoCan ) )
				||
				( 
					Config._jails.officersCanJail._ && Utilities.isOfficer( whoCan ) 
					|| Config._jails.leadersCanJail._ && Utilities.isLeader( whoCan ) 
				) 
			    && 
				( 
				  (!Config._jails.furtherRestrictJailUnjailToThoseThatHavePermission._) || 
						FactionsPlus.permission.playerHas( Utilities.getOnlinePlayerExact(whoCan), permissionNodeNameForCanJailUnjail ) 
				)
			   );
	}
	
}
