package markehme.factionsplus.Cmds;

import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.listeners.*;

import org.bukkit.*;
import org.bukkit.entity.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;
import com.massivecraft.factions.struct.*;


public class CmdPowSettings extends FCommand {
	
	public CmdPowSettings() {
		this.aliases.add( "powsets" );
		this.aliases.add( "powsettings" );
		this.aliases.add( "powersettings" );

		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		this.errorOnToManyArgs = true;
		
		this.setHelpShort( "show the settings for power loss or gains" );
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
	}
	
	private static final ChatColor msgColor1 = ChatColor.YELLOW;
	private static final ChatColor goodColor=ChatColor.GREEN;
	private static final ChatColor badColor=ChatColor.RED;
	private static final ChatColor numColor=ChatColor.AQUA;
	private static final ChatColor	deathByColor	= ChatColor.DARK_PURPLE;
	
	private static final String _do_colorless="do"+msgColor1;
	private static final String _dont_colorless="do not"+msgColor1;
	private static final String _goodDO=ChatColor.GREEN+_do_colorless;
	private static final String _badDO=ChatColor.RED+_do_colorless;
	private static final String _goodDONT=ChatColor.GREEN+_dont_colorless;
	private static final String _badDONT=ChatColor.RED+_dont_colorless;
	
	
	@Override
	public void perform() {
		//TODO: split into pages? to avoid many msgs sent at once, maybe some plugins will prevent those msgs to ever be sent
		
		sm(ChatColor.GRAY,"---Factions+FactionsPlus power settings/stats---");
		
		sm("Factions "+(Conf.powerFactionMax>0.0?badColor+"are limited to "+msgColor1+Conf.powerFactionMax:
			_goodDONT+" have a limit on")+" max power.");

		sm("Player power min/starting/max: "+num(Conf.powerPlayerMin)+"/"+num(Conf.powerPlayerStarting)+"/"+num(Conf.powerPlayerMax));

		sm("Players lose "+num(Conf.powerPerDeath)+" power per death."+(Config._powerboosts.enabled._?" Add to this the extra loss from below:":" Extra losses/gains are not enabled."));
		// done: show how much power will be lost in all cases (consider Factions+FactionsPlus settings)
		if ( Config._powerboosts.enabled._ ) {
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathBySuicide._, "suicide" );
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathByCactus._, "cactus" );
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathByFire._, "fire" );
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathByMob._, "mob" );
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathByPotion._, "potion" );
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathByPVP._, "PVP/player" );
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathByTNT._, "TNT" );
			showExtraLoss( Config._powerboosts.extraPowerLossIfDeathByOther._, "other" );
			showExtraGain( Config._powerboosts.extraPowerWhenKillMonster._, "mob" );
			showExtraGain( Config._powerboosts.extraPowerWhenKillPlayer._, "PVP/player" );
		}

		sm("Player regenerates "+num(Conf.powerPerMinute)+" power per minute.");

		sm( "While offline, players " + ( Conf.powerRegenOffline ? _goodDO : _badDONT )
			+ " regenerate and they "+
				(Conf.powerOfflineLossPerDay>0.0?badColor+
				"also lose "+msgColor1+num(Conf.powerOfflineLossPerDay)+" power per day" +
				" but only if their power is above "+num(Conf.powerOfflineLossLimit)
				:_goodDONT+" lose any power.")
				);
		
		sm("Players "+(Conf.canLeaveWithNegativePower?goodColor+"can":badColor+"cannot")+
			msgColor1+" join/leave or be kicked from factions if their power is negative.");
		
		boolean canLosePowerInWarZone = Utilities.confIs_warzonePowerLoss();//this means any warzone of even nopowerloss worlds
		sm("Players "+(canLosePowerInWarZone?_badDO:_goodDONT)+" lose power if they died in any world's WarZone.");
		
		//TODO: maybe not: show how much power will be lost in all cases (consider Factions+FactionsPlus settings) if player were to die in current spot- tough
		if (sender instanceof Player) {
			Player player = (Player)sender;
			FPlayer fplayer=FPlayers.i.get( player );
			sm("Your exact current power is "+num(fplayer.getPower()));
			
			boolean noLossWorld=Utilities.noPowerLossWorld( player.getWorld() );
			String worldName=ChatColor.GRAY+"("+player.getWorld().getName()+")"+msgColor1;
			sm("Players "+(noLossWorld?_goodDONT:_badDO )+
				" lose power if they died in this"+worldName+" world"+
					(noLossWorld&&canLosePowerInWarZone?badColor+"(except inside its WarZone)":
						(noLossWorld?"(not even in WarZone)":"(including in its WarZone)"))+".");
			
			sm("Players "+(Utilities.confIs_wildernessPowerLoss()?_badDO:_goodDONT)+" lose power if they died in any world's "+
					Factions.i.getNone().getTag()+
					(noLossWorld?msgColor1+"("+goodColor+"except"+msgColor1+" in the world that you are currenrly in"+worldName+")"
						:msgColor1+"(including the world you're in"+worldName+")"));

			
			sm("Estimating that you "+
					(!PowerboostListener.canLosePowerWherePlayerIsAt(player )?
						_goodDONT:_badDO)+
					" lose power if you died in this spot." );
		}
	}
	
	private void showExtraLoss( double extraLoss,String deathBy ) {
		showExtra("lose", extraLoss,"death by", deathBy);
	}
	
	private void showExtraGain( double extraGain,String killWhat ) {
		showExtra("gain", extraGain,"they kill", killWhat);
	}
	
	private void showExtra(String pre, double extraLoss,String preSuffix, String suffix ) {
		sm("Players "+pre+" extra "+num(extraLoss)+" power if "+preSuffix+" "+deathByColor+suffix+msgColor1+".");
	}

	private static final String num(double d) {
		return numColor+String.format(Locale.ENGLISH, "%1$,.2f", d)+ msgColor1;
	}
	
	private final void sm(String msg) {
		sm( msgColor1, msg );
	}
	
	private final void sm(ChatColor startColor, String msg) {
		sender.sendMessage( startColor+msg );
	}
}
