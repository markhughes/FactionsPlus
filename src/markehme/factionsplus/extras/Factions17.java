package markehme.factionsplus.extras;

import java.lang.reflect.*;

import markehme.factionsplus.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.*;



public class Factions17 extends FactionsBase implements FactionsAny {
	
	private Method			mSetFlag		= null;// Faction.setFlag(FFlag);
	private Class			classFFlag		= null;// FFlag.class
	private Object			enumPeaceful	= null;// FFlag.PEACEFUL as instance/enum
	private Field			fPeaceful		= null;// FFlag.PEACEFUL as field /useless
	
	
	protected Factions17( ) {
		super();
		
		boolean failed = false;
		
		try {
			classFFlag = Class.forName( "com.massivecraft.factions.struct.FFlag" );
			
			fPeaceful = classFFlag.getField( "PEACEFUL" );

			enumPeaceful = fPeaceful.get( classFFlag );//this is safe to get here, this soon.
			if ( null == enumPeaceful ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
			mSetFlag = Faction.class.getMethod( "setFlag", classFFlag, boolean.class );
			
		} catch ( NoSuchMethodException e ) {// multi catch could've worked but unsure if using jdk7 to compile
			e.printStackTrace();
			failed = true;
		} catch ( SecurityException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( NoSuchFieldException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlus.bailOut( "failed to hook into Factions 1.6.x" );
			}
		}
	}
	
	
	@Override
	public void setFlag( Faction forFaction, FactionsAny.FFlag whichFlag, Boolean whatState ) {
		boolean failed = false;
		try {
			Object flag = null;
			switch ( whichFlag ) {
			case PEACEFUL:
				flag = enumPeaceful;
				break;
			// add new flags here
			default:
				throw FactionsPlus.bailOut( "plugin author forgot to define a case to handle this flag: "
					+ whichFlag );
				// or forgot to put a "break;"
			}
			// factiont.setFlag(com.massivecraft.factions.struct.FFlag.PEACEFUL, true);
			mSetFlag.invoke( forFaction, flag, whatState );
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( InvocationTargetException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlus.bailOut( "failed to invoke " + mSetFlag );
			}
		}
	}
	
	
	@Override
	public final void finalizeHelp() {
		// not required for 1.7
		return;
	}
}
