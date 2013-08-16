package markehme.factionsplus.Cmds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.listeners.PowerboostListener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

public class CmdPowSettings extends FPCommand {
	
	public CmdPowSettings() {
		this.aliases.add( "powsets" );
		this.aliases.add( "powsettings" );
		this.aliases.add( "powersettings" );
		
		this.optionalArgs.put("page", "1");
		this.errorOnToManyArgs = true;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		
		this.setHelp( "show the settings for power loss or gains" );
		this.setDesc( "show the settings for power loss or gains" );

	}
	
	private static final ChatColor msgColor1 		= ChatColor.YELLOW;
	private static final ChatColor goodColor		= ChatColor.GREEN;
	private static final ChatColor badColor			= ChatColor.RED;
	private static final ChatColor numColor			= ChatColor.AQUA;
	private static final ChatColor	deathByColor	= ChatColor.DARK_PURPLE;
	
	private static final String _do_colorless		= "do"+msgColor1;
	private static final String _dont_colorless		= "do not"+msgColor1;
	private static final String _goodDO				= ChatColor.GREEN+_do_colorless;
	private static final String _badDO				= ChatColor.RED+_do_colorless;
	private static final String _goodDONT			= ChatColor.GREEN+_dont_colorless;
	private static final String _badDONT			= ChatColor.RED+_dont_colorless;
	
	
	private List<String> allLines					= new ArrayList<String>();
	
	@Override
	public void performfp() {
		
		msg("This has been broken in Factions 2.0.");
		
		return;
		/*
		allLines.clear();
		//done: split into pages? to avoid many msgs sent at once, maybe some plugins will prevent those msgs to ever be sent
		
//		sm(ChatColor.GRAY,"---Factions+FactionsPlus power settings/stats---");
		
		sm("Factions "+(UConf.get(usender).factionPowerMax>0.0?badColor+"are limited to "+msgColor1+UConf.get(usender).factionPowerMax:
			_goodDONT+" have a limit on")+" max power.");

		sm("Player power min/starting/max: "+num(UConf.get(usender).powerMin)+"/"+num(UConf.get(usender).defaultPlayerPower)+"/"+num(UConf.get(usender).powerMax));

		sm("Players lose "+num(UConf.get(usender).powerPerDeath)+" power per death."+(Config._powerboosts.enabled._?" Add to this the extra loss from below:":" Extra losses/gains are not enabled."));
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

		sm("Player regenerates "+num(UConf.get(usender).powerPerHour)+" power per hour.");
		
		
		sm( "While offline, players " + ( Conf.powerRegenOffline ? _goodDO : _badDONT )
			+ " regenerate and they "+
				(Conf.powerOfflineLossPerDay>0.0?badColor+
				"also lose "+msgColor1+num(Conf.powerOfflineLossPerDay)+" power per day" +
				" but only if their power is above "+num(Conf.powerOfflineLossLimit)
				:_goodDONT+" lose any power.")
				);
		
		
				
		sm("Players "+(UConf.get(usender).canLeaveWithNegativePower?goodColor+"can":badColor+"cannot")+
			msgColor1+" join/leave or be kicked from factions if their power is negative.");
		
		//boolean canLosePowerInWarZone = Utilities.confIs_warzonePowerLoss();//this means any warzone of even nopowerloss worlds
		//sm("Players "+(canLosePowerInWarZone?_badDO:_goodDONT)+" lose power if they died in any world's WarZone.");
		
		//TODO: maybe not: show how much power will be lost in all cases (consider Factions+FactionsPlus settings) if player were to die in current spot- tough
		if (sender instanceof Player) {
			Player player 		= (Player)sender;
			UPlayer fplayer 	= UPlayer.get( player );
			if (Utilities.isJailed( player )) {
				sm(ChatColor.RED+"You are currently in jail.");
			}
			sm("Your exact current power is "+num(fplayer.getPower()));
			
			
			
			boolean noLossWorld=Utilities.noPowerLossWorld( player.getWorld() );
			String worldName=ChatColor.GRAY+"("+player.getWorld().getName()+")"+msgColor1;
			sm("Players "+(noLossWorld?_goodDONT:_badDO )+
				" lose power if they died in this"+worldName+" world"+
				(noLossWorld&&true?badColor+"(except inside its WarZone)":
				//TODO: reinstate this -> (noLossWorld&&canLosePowerInWarZone?badColor+"(except inside its WarZone)":
						(noLossWorld?"(not even in WarZone)":"(including in its WarZone)"))+".");
			
			sm("Players "+(Utilities.confIs_wildernessPowerLoss()?_badDO:_goodDONT)+" lose power if they died in any world's "+
					Faction.get("0").getName()+
					(noLossWorld?msgColor1+"("+goodColor+"except"+msgColor1+" in the world that you are currenrly in"+worldName+")"
						:msgColor1+"(including the world you're in"+worldName+")"));

			
			sm("Estimating that you "+
					(!PowerboostListener.canLosePowerWherePlayerIsAt(player )?
						_goodDONT:_badDO)+
					" lose power if you died in this spot." );
		}
		
		
		sm("Players & faction-warps "+(Config._warps.mustBeInOwnTerritoryToCreate._?goodColor+"have to":badColor+"don't have to")+
			msgColor1+" be in the player's own faction's territory for create/teleport-to.");
		
		sendMessage(Txt.getPage(allLines, this.getArgs().size(), "Power settings&stats, page: "));
		*/
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
		return num(Double.valueOf(d));
	}
	
	private static final String num(Double d) {
		return numColor+String.format(Locale.ENGLISH, "%1$,.2f", d)+ msgColor1;
	}
	
	private final void sm(String msg) {
		sm( msgColor1, msg );
	}
	
	private final void sm(ChatColor startColor, String msg) {
//		sender.sendMessage( startColor+msg );
		allLines.add( startColor+msg  );
	}
}
