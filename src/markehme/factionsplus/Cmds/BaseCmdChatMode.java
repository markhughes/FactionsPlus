package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.FactionsBridge.Bridge;
import markehme.factionsplus.FactionsBridge.FactionsAny;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Permission;


public abstract class BaseCmdChatMode extends FPCommand{
	protected final FactionsAny.ChatMode cMode;
	private final String strPermission;

	protected BaseCmdChatMode( FactionsAny.ChatMode cm, String perm, String... arrayOfAliases) {
		cMode=cm;
		if (0 >= arrayOfAliases.length) {
			FactionsPlusPlugin.bailOut( "coder forgot to add at least one alias for a command "+this.getClass() );
		}
		
		for ( int i = 0; i < arrayOfAliases.length; i++ ) {
			this.aliases.add(arrayOfAliases[i]);
		}
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("set your chat to "+cMode.getDescription());
		
		strPermission=perm;
	}
	
	@Override
	public final void performfp() {
		if(!FactionsPlus.permission.has(Utilities.getOnlinePlayerExact( fme), strPermission )) {
			fme.msg(ChatColor.RED+"No permission!");
			return;
		}	
		changeChatMode(fme,cMode);
	}
	
	public final void changeChatMode(FPlayer forWhatPlayer, FactionsAny.ChatMode chatMode) {
//		if ( ! Conf.factionOnlyChat )
//		{
//			msg("<b>The built in chat chat channels are disabled on this server.");
//			return;
//		}
		
		FactionsAny.ChatMode from=Bridge.factions.setChatMode(forWhatPlayer, chatMode );
		//a null here means, we're using 1.7 and setChatMode is not supported
		if ( null!=from ) {
			// fme.setChatMode(com.massivecraft.factions.struct.ChatMode.FACTION);
			markehme.factionsplus.FactionsBridge.FactionsAny.ChatMode modeNow = Bridge.factions.getChatMode(forWhatPlayer);
			
			fme.msg( "Your chat mode "+
					(modeNow.equals( from )?"is still on:"
						:"has been changed from "+from.getDescription()+" to")
				+" <i>"+modeNow.getDescription()+"." );
		} else {
			fme.msg( "This version of Factions does not support changing chat mode." );
		}
	}


}
