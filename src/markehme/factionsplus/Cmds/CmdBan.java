package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.config.sections.Section_Banning;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdBan extends FPCommand {
	public CmdBan() {
		this.aliases.add( "ban" );
		
		this.requiredArgs.add( "player" );
		this.errorOnToManyArgs = true;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "kicks a player out of your Faction, and stops them from re-joining" );
		this.setDesc( "kicks a player out of your Faction, and stops them from re-joining" );
		
	}

	@Override
	public void performfp() {
		String banningThisPlayer = this.arg(0);
		
		Faction pFaction = usender.getFaction();
		
		assert null != pFaction;

		if( ( Config._banning.furtherRestrictBanUnBanToThoseThatHavePermission._ )
				&&( ! FactionsPlus.permission.has(sender, Section_Banning.banUnBanPermissionNodeName ) ) ) {
			
			usender.msg( ChatColor.RED + "You don't have the required permission node!" );
			
			return;
		}
		
		if (! 
				( (Config._banning.leadersCanFactionBanAndUnban._ && Utilities.isLeader(usender)) 
				||(Config._banning.officersCanFactionBanAndUnban._ && Utilities.isOfficer(usender)) ) 
		  ) {
			
			usender.msg( ChatColor.RED + "Sorry, your ranking is not high enough to do that!" );
			
			return;
		}
		
		Player playerBanThisPlayer = Utilities.getOnlinePlayerExact( banningThisPlayer );

		if ( UPlayer.get( Bukkit.getPlayerExact( banningThisPlayer ) ) == null ) {
			
			me.sendMessage( ChatColor.RED + "That player was never on the server" );
			
			return;
		}
		
		UPlayer fPlayerBanThisPlayer = UPlayer.get( Bukkit.getPlayerExact( banningThisPlayer ) ); // online or offline, doesn't matter

		File banFile = new File( Config.folderFBans, pFaction.getId() + "." + banningThisPlayer.toLowerCase() );
	
		// are we dealing with the same faction ? 
		if( ! fPlayerBanThisPlayer.getFactionId().equalsIgnoreCase( usender.getFactionId() ) ) {
			if( banFile.exists() ) {
				me.sendMessage("This user is already banned from the Faction!");
				
				return;
			}
			
			// Only allow banning if this is true
			if ( ! Config._banning.canBanToPreventFutureJoins._ ) {
				
				me.sendMessage("You can only ban players that exist in your faction");
				return;
			}
		} else {
			// user is in faction, cause it to leave first
			if(Utilities.isLeader(fPlayerBanThisPlayer) || fPlayerBanThisPlayer.isUsingAdminMode() ) {
				me.sendMessage("You can't ban the leader of the Faction!");
				
				return;
			}
			
			if ( !UConf.get( usender ).canLeaveWithNegativePower && fPlayerBanThisPlayer.getPower() < 0 ) {
				msg( "<b>You cannot ban that player until their power is positive." );
				return;
			}
			
			
			FactionsEventMembershipChange event = new FactionsEventMembershipChange(sender, fPlayerBanThisPlayer, FactionColls.get().get(fPlayerBanThisPlayer).getNone(), MembershipChangeReason.KICK);
			event.run();
			
			if (event.isCancelled()) return;
			
			// Uninvite the player from the Faction if they're still invited 
			if(usender.getFaction().isInvited(fPlayerBanThisPlayer)) {
				usender.getFaction().setInvited(fPlayerBanThisPlayer, false);
			}
			
		}
		
		boolean isInFaction = fPlayerBanThisPlayer.getFactionId().equalsIgnoreCase(usender.getFactionId());
		
		pFaction.msg( "%s<i> banned %s<i> from "+(isInFaction?"":"future joining")+" the faction!", usender.describeTo( pFaction, true ),
		
		fPlayerBanThisPlayer.describeTo( pFaction, true ) );
		
		if ( MConf.get().logFactionKick ) {
			
			Factions.get().log( ( senderIsConsole ? "A console command" : usender.getName() ) + " banned " + fPlayerBanThisPlayer.getName()
				+ " from "+(isInFaction?"the":"future joining the")+" faction: " + pFaction.getName() );
		
		}
		
		
		if (isInFaction) {
			fPlayerBanThisPlayer.resetFactionData();
		}
		
		try {
			banFile.createNewFile();
			
			if ( null != playerBanThisPlayer ) {
				
				if(Config._jails.tellPlayerWhoBannedThem._) {
					playerBanThisPlayer.sendMessage( "You were banned from the faction "+usender.getFaction().getName()+" by " + usender.getName() + "." );
				} else {
					playerBanThisPlayer.sendMessage( "You were banned from the faction "+usender.getFaction().getName()+".");
				}
				
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			msg( ChatColor.RED + "Internal error, probably failed to ban the player " + banningThisPlayer );
		}

	}
}
