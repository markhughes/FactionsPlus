package markehme.factionsplus.Cmds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.sublisteners.PowerBoostSubListener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdPowSettings extends FPCommand {
	
	public CmdPowSettings() {
		this.aliases.add("powsets");
		this.aliases.add("powsettings");
		this.aliases.add("powersettings");
		this.aliases.add("powsettings");
		this.aliases.add("powsetting");
		
		this.fpidentifier = "powersettings";
		
		this.optionalArgs.put("page", "1");
		this.errorOnToManyArgs = true;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
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
	private static final String _badDO				= ChatColor.RED+_do_colorless;
	private static final String _goodDONT			= ChatColor.GREEN+_dont_colorless;
	
	private List<String> allLines					= new ArrayList<String>();
	
	@Override
	public void performfp() {
		
		allLines.clear();
		
		sm(ChatColor.DARK_PURPLE,"----- " + ChatColor.GREEN + "power settings/statistics" + ChatColor.DARK_PURPLE + " -----");
		
		sm("Factions "+(UConf.get(usender).factionPowerMax>0.0?badColor+"are limited to "+msgColor1+UConf.get(usender).factionPowerMax:
			_goodDONT+" have a limit on")+" max power.");

		sm("Player power min/starting/max: "+num(UConf.get(usender).powerMin)+"/"+num(UConf.get(usender).defaultPlayerPower)+"/"+num(UConf.get(usender).powerMax));
		
		Boolean extraPowerLossGainEnabled = false;
		if (
				fpuconf.extraPowerLoss.get("whenDeathBySuicide") > 0
				|| fpuconf.extraPowerLoss.get("whenDeathByPVP") > 0
				|| fpuconf.extraPowerLoss.get("whenDeathByMob") > 0
				|| fpuconf.extraPowerLoss.get("whenDeathByCactus") > 0
				|| fpuconf.extraPowerLoss.get("whenDeathByTNT") > 0
				|| fpuconf.extraPowerLoss.get("whenDeathByFire") > 0
				|| fpuconf.extraPowerLoss.get("whenDeathByPotion") > 0
				|| fpuconf.extraPowerLoss.get("whenDeathByOther") > 0
				|| fpuconf.extraPowerBoosts.get("whenKillEnemyPlayer")>0
				|| fpuconf.extraPowerBoosts.get("whenKillAllyPlayer")>0
				|| fpuconf.extraPowerBoosts.get("whenKillTrucePlayer")>0
				|| fpuconf.extraPowerBoosts.get("whenKillNeutralPlayer")>0
				|| fpuconf.extraPowerBoosts.get("whenKillAnotherMonster")>0
				
		) {
			extraPowerLossGainEnabled = true;
		}
		
		sm("Players lose "+num(UConf.get(usender).powerPerDeath)+" power per death."+(extraPowerLossGainEnabled ?" Add to this the extras from below:":" Extra losses/gains are not enabled."));
		
		if (extraPowerLossGainEnabled) {
			
			if(fpuconf.extraPowerLoss.get("whenDeathBySuicide") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathBySuicide"), "suicide");
			if(fpuconf.extraPowerLoss.get("whenDeathByCactus") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathByCactus"), "cactus" );
			if(fpuconf.extraPowerLoss.get("whenDeathByFire") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathByFire"), "fire" );
			if(fpuconf.extraPowerLoss.get("whenDeathByMob") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathByMob"), "mob" );
			if(fpuconf.extraPowerLoss.get("whenDeathByPotion") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathByPotion"), "potion" );
			if(fpuconf.extraPowerLoss.get("whenDeathByPVP") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathByPVP"), "PVP/player" );
			if(fpuconf.extraPowerLoss.get("whenDeathByPVP") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathByTNT"), "TNT" );
			if(fpuconf.extraPowerLoss.get("whenDeathByOther") >0) showExtraLoss(fpuconf.extraPowerLoss.get("whenDeathByOther"), "other" );
			
			if(fpuconf.extraPowerBoosts.get("whenKillEnemyPlayer") >0) showExtraGain(fpuconf.extraPowerBoosts.get("whenKillEnemyPlayer"), "enemy players" );
			if(fpuconf.extraPowerBoosts.get("whenKillAllyPlayer") >0) showExtraGain(fpuconf.extraPowerBoosts.get("whenKillAllyPlayer"), "ally players" );
			if(fpuconf.extraPowerBoosts.get("whenKillTrucePlayer") >0) showExtraGain(fpuconf.extraPowerBoosts.get("whenKillTrucePlayer"), "truce players" );
			if(fpuconf.extraPowerBoosts.get("whenKillNeutralPlayer") >0) showExtraGain(fpuconf.extraPowerBoosts.get("whenKillNeutralPlayer"), "neutral players" );
			if(fpuconf.extraPowerBoosts.get("whenKillAnotherMonster") >0) showExtraGain(fpuconf.extraPowerBoosts.get("whenKillAnotherMonster"), "mob" );

		}

		sm("Player regenerates "+num(UConf.get(usender).powerPerHour)+" power per hour.");
		
		UConf uconf 		= UConf.get(universe);
		
		/*
		sm( "While offline, players " + ( Conf.powerRegenOffline ? _goodDO : _badDONT )
			+ " regenerate and they "+
				(Conf.powerOfflineLossPerDay>0.0?badColor+
				"also lose "+msgColor1+num(Conf.powerOfflineLossPerDay)+" power per day" +
				" but only if their power is above "+num(Conf.powerOfflineLossLimit)
				:_goodDONT+" lose any power.")
				);
		
		*/
				
		sm("Players "+(uconf.canLeaveWithNegativePower?goodColor+"can":badColor+"cannot")+
			msgColor1+" join/leave or be kicked from factions if their power is negative.");
		
		boolean canLosePowerInWarZone = Faction.get(uconf.factionIdWarzone).getFlag(FFlag.POWERLOSS);
		sm("Players "+(canLosePowerInWarZone?_badDO:_goodDONT)+" lose power if they died in any world's WarZone.");
		
		if(sender instanceof Player) {
			
			Player player 		= (Player) sender;
			UPlayer fplayer 	= UPlayer.get(player);
			FactionData fdata	= FactionDataColls.get().getForUniverse(fplayer.getUniverse()).get(fplayer.getFactionId());
			
			if (fdata.jailedPlayerIDs.containsKey(player.getUniqueId())) {
				sm(ChatColor.RED+"You are currently in jail.");
			}
			
			sm("Your exact current power is "+num(fplayer.getPower()));
			
			boolean noLossWorld = (uconf.powerPerDeath==0.0);
			
			String worldName=ChatColor.GRAY+"("+player.getWorld().getName()+")"+msgColor1;
			sm("Players "+(noLossWorld?_goodDONT:_badDO )+
				" lose power if they died in this"+worldName+" world"+
				(noLossWorld?badColor+"(except inside its WarZone)":
				(noLossWorld&&Faction.get(uconf.factionIdWarzone).getFlag(FFlag.POWERLOSS)?badColor+"(except inside its WarZone)":
						(noLossWorld?"(not even in WarZone)":"(including in its WarZone)"))+"."));
			
			sm("Players "+(Faction.get(uconf.factionIdNone).getFlag(FFlag.POWERLOSS)?_badDO:_goodDONT)+" lose power if they died in any world's "+
					Faction.get("0").getName()+
					(noLossWorld?msgColor1+"("+goodColor+"except"+msgColor1+" in the world that you are currenrly in"+worldName+")"
						:msgColor1+"(including the world you're in"+worldName+")"));

			
			sm("Estimating that you "+
					(!PowerBoostSubListener.canLosePowerWherePlayerIsAt(player )?
						_goodDONT:_badDO)+
					" lose power if you died in this spot." );
		}
		
		List<String> allowedWarpsIn = new ArrayList<String>();
		
		if(fpuconf.allowWarpsIn.get("owned")) {
			allowedWarpsIn.add("Own Territory");
		}
		if(fpuconf.allowWarpsIn.get("wilderness")) {
			allowedWarpsIn.add("The Wilderness");
		}
		if(fpuconf.allowWarpsIn.get("safezone")) {
			allowedWarpsIn.add("SafeZone's");
		}
		if(fpuconf.allowWarpsIn.get("warzone")) {
			allowedWarpsIn.add("WarZone's");
		}
		if(fpuconf.allowWarpsIn.get("ally")) {
			allowedWarpsIn.add("Ally Factions Land");
		}
		if(fpuconf.allowWarpsIn.get("enemy")) {
			allowedWarpsIn.add("Enemy Factions Land");
		}
		if(fpuconf.allowWarpsIn.get("neutral")) {
			allowedWarpsIn.add("Neutral Factions Land");
		}
		if(fpuconf.allowWarpsIn.get("truce")) {
			allowedWarpsIn.add("Truce Factions Territory");
		}
		
		
		int i = 1;
		String warpsMessage = "You are allowed warps in";
		for(String l : allowedWarpsIn) {
			if(allowedWarpsIn.size() == 1) {
				warpsMessage += " " + l + ".";
			} else if(i == allowedWarpsIn.size()) {
				warpsMessage += " and " + l + ".";
			} else {
				warpsMessage += " " + l + ",";
			}
			i++;
		}
		
		if(allowedWarpsIn.size() != 0) {
			sm(warpsMessage);
		}
		
		sendMessage(Txt.getPage(allLines, this.getArgs().size(), "Power settings&stats, page: "));
		
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
		allLines.add( startColor+msg  );
	}
}
