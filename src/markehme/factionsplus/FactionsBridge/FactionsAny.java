package markehme.factionsplus.FactionsBridge;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.iface.RelationParticipator;



public interface FactionsAny {
	
	
	public static enum FFlag {
		PEACEFUL,
		POWERLOSS,
		// adding new flags here requires adding switch-cases in two locations, do Ctrl+Alt+H on one of the flags to track where
		//this is mainly because 1.6 requires a different method to be called for setting each flag
		//the above are considered handled, the below are not yet
		
		PERMANENT,
		INFPOWER,
		PVP,
		FRIENDLYFIRE,
		MONSTERS,
		EXPLOSIONS,
		FIRESPREAD,
		ENDERGRIEF,
	}
	
	public static enum Relation {
		LEADER, // only 1.7 has this 
		OFFICER, // only 1.7 has this
		TRUCE, // only 1.7 has this

		MEMBER,//both 1.6 and 1.7 have this 
		ALLY, //both 1.6 and 1.7 have this
		NEUTRAL, //both 1.6 and 1.7 have this
		ENEMY //both 1.6 and 1.7 have this

		//while you can use these in any case (regardless of Factions v 1.6 or 1.7) you should know that if you're 
		//expecting a 1.7 flag like LEADER while using 1.6 Factions, you won't ever see it returned, though you may find
		//yourself wrongly comparing against it (in this case a comparison which will always be false while using 1.6)
	}
	
	public static enum ChatMode {
		FACTION( "Faction-only" ), 
		ALLIANCE( "Alliance-only" ), 
		PUBLIC( "Public(Global)" );//this basically just means you're not in any of FACTION or ALLIANCE chats
		
		private String	description;//description will be used in when showing info/help to the player
		
		
		private ChatMode( String desc ) {
			description = desc;
		}
		
		
		public String getDescription() {
			return description;
		}
		
		
		@Override
		public String toString() {
			return description;
		}
	}
	
	public void setFlag( Faction forFaction, FactionsAny.FFlag whichFlag, Boolean whatState );
	public boolean getFlag( Faction forFaction, FactionsAny.FFlag whichFlag);
	
	
	public FactionsAny.Relation getRelationBetween( RelationParticipator one, RelationParticipator two );
	
	
	/**
	 * @param one
	 * @return can never return null
	 */
	public FactionsAny.Relation getRole( RelationParticipator one);
	
	/**
	 * this should only ever be called after {@link #addSubCommand(FCommand)} as the last statement<br>
	 * this will update the help just in case last commands didn't fit the page entirely<br>
	 */
	public void finalizeHelp();
	
	/**
	 * @param forWhatPlayer the FPlayer whose chat mode we're setting
	 * @param chatMode
	 * @return null if nothing was done(aka not implemented) ie. it's 1.7 and this has no effect because 1.7 doesn't
	 * support changing chat mode<br>
	 * 		or just the previous chat mode
	 */
	public FactionsAny.ChatMode setChatMode(FPlayer forWhatPlayer, FactionsAny.ChatMode chatMode);


	public FactionsAny.ChatMode getChatMode(FPlayer forWhatPlayer);


	/**
	 * use this instead of the FCommand fields:<br>
	 * senderMustBeAdmin  (Factions 1.6.x only)<br>
	 * senderMustBeLeader  (Factions 1.7.x only)<br>
	 * @param fCommandInstance
	 * @param flagState
	 */
	public void setSenderMustBeFactionAdmin( FCommand fCommandInstance, boolean flagState );


	void addSubCommand( FCommand base, FCommand subCommand );
	
	public boolean isFactions17();
}
