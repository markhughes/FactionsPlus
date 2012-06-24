package markehme.factionsplus.extras;

import java.lang.reflect.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;



public interface FactionsAny {
	
	public static enum FFlag {
		PEACEFUL
		// adding new flags here requires adding switch-cases in two locations, do Ctrl+Alt+H on one of the flags to track where
		//this is mainly because 1.6 requires a different method to be called for setting each flag
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
		FACTION,
		ALLIANCE,
		PUBLIC
	}
	
	public void setFlag( Faction forFaction, FactionsAny.FFlag whichFlag, Boolean whatState );
	
	
	public FactionsAny.Relation getRelationTo( FPlayer one, FPlayer two );
	
	
	public void addSubCommand( FCommand subCommand );
	
	
	/**
	 * this should only ever be called after {@link #addSubCommand(FCommand)} as the last statement<br>
	 * this will update the help just in case last commands didn't fit the page entirely<br>
	 */
	public void finalizeHelp();
	
	/**
	 * @param sender the FPlayer to inform of any messages
	 * @param chatMode
	 * @return false if nothing was done(aka not implemented) ie. it's 1.7 and this has no effect because 1.7 doesn't
	 * support changing chat mode
	 */
	public boolean setChatMode(FactionsAny.ChatMode chatMode);
}
