package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.config.sections.Section_Banning;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.struct.Permission;

public class CmdBan extends FPCommand {
	public CmdBan() {
		this.aliases.add("ban");

		this.requiredArgs.add("player");

		//this.optionalArgs.put("on/off", "flip");

		this.errorOnToManyArgs = true;

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;

		senderMustBePlayer = true;
		senderMustBeMember = true;

		this.setHelpShort("kicks a player out of your Faction, and stops them from re-joining");
	}

	@Override
	public void performfp() {
		String banningThisPlayer = this.argAsString(0);
		Faction pFaction = fme.getFaction();
		assert null != pFaction;

		if ((Config._banning.furtherRestrictBanUnBanToThoseThatHavePermission._)
				&&(!FactionsPlus.permission.has(sender, Section_Banning.banUnBanPermissionNodeName))) {
			sender.sendMessage(ChatColor.RED + "You don't have the required permission node!");
			return;
		}
		
		if (! 
				( (Config._banning.leadersCanFactionBanAndUnban._ && Utilities.isLeader(fme)) 
				||(Config._banning.officersCanFactionBanAndUnban._ && Utilities.isOfficer(fme)) ) 
		  ) {
			fme.msg(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}

		
		
		Player playerBanThisPlayer = Utilities.getOnlinePlayerExact(banningThisPlayer);

		if (!FPlayers.i.exists( banningThisPlayer )) {
			me.sendMessage(ChatColor.RED+"That player was never on the server");
			return;
		}
		
		FPlayer fPlayerBanThisPlayer = FPlayers.i.get(banningThisPlayer);//online or offline, doesn't matter


		File banFile = new File(Config.folderFBans, pFaction.getId() + "." + banningThisPlayer.toLowerCase());

			
		boolean isInFaction = fPlayerBanThisPlayer.getFactionId().equalsIgnoreCase(fme.getFactionId());
		if(!isInFaction) {
			if(banFile.exists()) {
				me.sendMessage("This user is already banned from the Faction!");
				return;
			}
			if (!Config._banning.canBanToPreventFutureJoins._) {
				me.sendMessage("You can only ban players that exist in your faction");
				return;
			}
		} else {
			// user is in faction, cause it to leave first
			if(Utilities.isLeader(fPlayerBanThisPlayer)) {
				me.sendMessage("You can't ban the leader of the Faction!");
				return;
			}
			
			if ( !Conf.canLeaveWithNegativePower && fPlayerBanThisPlayer.getPower() < 0 ) {
				msg( "<b>You cannot ban that player until their power is positive." );
				return;
			}
			
			// fPlayerBanThisPlayer.leave(false);//false to not charge money, this call isn't good enough...
			
			// shamelessly copy/pasted some code from Factions' kick command below:
			// trigger the leave event (cancellable) [reason:kicked]
			FPlayerLeaveEvent event =
				new FPlayerLeaveEvent( fPlayerBanThisPlayer, fPlayerBanThisPlayer.getFaction(),
					FPlayerLeaveEvent.PlayerLeaveReason.KICKED );// but this may be denied by whoever wants to deny kick but not
																	// necessarily ban
			Bukkit.getServer().getPluginManager().callEvent( event );
			if ( event.isCancelled() )
				return;
			
		}
			// this is still bugged when two players exist in the factions named like "s" and "s2" and "s" is offline, "s2" will
			// receive two msgs
			// due to Factions using getPlayer() instead of getPlayerExact()
		pFaction.msg( "%s<i> banned %s<i> from "+(isInFaction?"":"future joining")+" the faction! :O", fme.describeTo( pFaction, true ),
			fPlayerBanThisPlayer.describeTo( pFaction, true ) );
			// playerBanThisPlayer.sendMessage( "%s<i> banned you from %s<i>! :O", fme.describeTo( fPlayerBanThisPlayer, true ),
			// pFaction.describeTo( fPlayerBanThisPlayer ) );
			// if ( pFaction != myFaction )// FIXME: allow admin bypass or OP to f ban anyone in any faction (much to be
			// considered on this)
			// {
			// fme.msg( "<i>You banned %s<i> from the faction %s<i>!", fPlayerBanThisPlayer.describeTo( fme ),
			// pFaction.describeTo( fme ) );
			// }
		
		if ( Conf.logFactionKick )
			P.p.log( ( senderIsConsole ? "A console command" : fme.getName() ) + " banned " + fPlayerBanThisPlayer.getName()
				+ " from "+(isInFaction?"the":"future joining the")+" faction: " + pFaction.getTag() );
		
		//this is reached if console or op (from other faction) caused the ban (situation is not yet implemented/allowed though, see fixme above)
//		if ( FactionsAny.Relation.LEADER.equals(Bridge.factions.getRole( fPlayerBanThisPlayer)) )
//			pFaction.promoteNewLeader();
		
		pFaction.deinvite( fPlayerBanThisPlayer );
		if (isInFaction) {
			fPlayerBanThisPlayer.resetFactionData();
		}
		
//		File banFile = new File(Config.folderFBans, pFaction.getId() + "." + banningThisPlayer.toLowerCase());

//		if(banFile.exists()) {
//			//this will happen if, the ban file existed due to being manually created, or by ban command failing to cause player 
//			//to leave above or simply factions.conf being restored from backup
//			me.sendMessage("This user is already banned from the Faction!");
//			return;
//		} else {
		try {
			banFile.createNewFile();
			if ( null != playerBanThisPlayer ) {// ie. he's online
				playerBanThisPlayer.sendMessage( "You were banned from the faction "+fme.getFaction().getTag()+" by " + fme.getName() );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			me.sendMessage( ChatColor.RED + "Internal error, probably failed to ban the player " + banningThisPlayer );
		}
//		}

	}
}
