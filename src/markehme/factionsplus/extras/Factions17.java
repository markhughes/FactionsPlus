package markehme.factionsplus.extras;

import java.lang.reflect.*;

import markehme.factionsplus.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.*;



public class Factions17 extends FactionsBase implements FactionsAny {
	
	private Method			mSetFlag		= null;
	private FactionsPlus	fpInst			= null;
	private Class			classFFlag		= null;
	private Object			enumPeaceful	= null;
	private Field			fPeaceful		= null;
	
	
	// factiont.setFlag(com.massivecraft.factions.struct.FFlag.PEACEFUL, true);
	protected Factions17( FactionsPlus fpInstance ) {
		super( fpInstance );
		
		boolean failed = false;
		
		try {
			classFFlag = Class.forName( "com.massivecraft.factions.struct.FFlag" );
			// FIXME: remove such redundant 'if' checks where it clearly only throws instead of being ever null
			if ( null == classFFlag ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
			fPeaceful = classFFlag.getField( "PEACEFUL" );
			if ( null == fPeaceful ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			enumPeaceful = fPeaceful.get( classFFlag );
			if ( null == enumPeaceful ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
			mSetFlag = Faction.class.getMethod( "setFlag", classFFlag, boolean.class );
			if ( null == mSetFlag ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
			// classRP=Class.forName( "com.massivecraft.factions.iface.RelationParticipator");
			// if (null == classRP) {//this is likely never null, it would rather just throw instead
			// failed=true;
			// return;
			// }
			//
			// mGetRelationTo=FPlayer.class.getMethod("getRelationTo",classRP);
			// if (null == mGetRelationTo) {//this is likely never null, it would rather just throw instead
			// failed=true;
			// return;
			// }
			//
			// classRel=Class.forName( "com.massivecraft.factions.struct.Rel");
			// if (null == classRel) {//this is likely never null, it would rather just throw instead
			// failed=true;
			// return;
			// }
			//
			// fRelEnemy=classRel.getField( "ENEMY" );
			// if (null == fRelEnemy) {//this is likely never null, it would rather just throw instead
			// failed=true;
			// return;
			// }
			//
			// enumRelEnemy=fRelEnemy.get( classRel );
			// if (null == enumRelEnemy) {//this is likely never null, it would rather just throw instead
			// failed=true;
			// return;
			// }
			
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
				throw FactionsPlus.bailOut( fpInst, "failed to hook into Factions 1.6.x" );
			}
		}
	}
	
	
	// @Override
	// public void setPeaceful(Faction faction, Boolean state ) {
	// boolean failed = false;
	// try {
	// mSetFlag.invoke(faction, enumPeaceful, state );
	// } catch ( IllegalAccessException e ) {
	// e.printStackTrace();
	// failed=true;
	// } catch ( IllegalArgumentException e ) {
	// e.printStackTrace();
	// failed=true;
	// } catch ( InvocationTargetException e ) {
	// e.printStackTrace();
	// failed=true;
	// }finally{
	// if (failed) {
	// throw FactionsPlus.bailOut( fpInst, "failed to invoke "+mSetFlag );
	// }
	// }
	// }
	
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
				throw FactionsPlus.bailOut( fpInst, "plugin author forgot to define a case to handle this flag: "
					+ whichFlag );
				// or forgot to put a "break;"
			}
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
				throw FactionsPlus.bailOut( fpInst, "failed to invoke " + mSetFlag );
			}
		}
	}
	
	
	@Override
	public final void finalizeHelp() {
		//not required for 1.7
	}
}
