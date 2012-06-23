package markehme.factionsplus.extras;

import java.lang.reflect.*;

import markehme.factionsplus.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;



public abstract class FactionsBase implements FactionsAny {
	
	protected FactionsPlus	fpInst;
	private Class			classRP			= null;
	private Class			classRel		= null;
	private Field			fRelEnemy		= null;
	private Object			enumRelEnemy	= null;
	private Method			mGetRelationTo	= null;
	
	
	protected FactionsBase( FactionsPlus fpInstance ) {
		boolean failed = false;
		fpInst = fpInstance;
		assert null != fpInst;
		
		try {
			classRP = Class.forName( "com.massivecraft.factions.iface.RelationParticipator" );
			if ( null == classRP ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
			mGetRelationTo = FPlayer.class.getMethod( "getRelationTo", classRP );
			if ( null == mGetRelationTo ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}

			classRel = Class.forName( "com.massivecraft.factions.struct."+(Factions16.class.equals( this.getClass() )?"Relation":"Rel") );
			if ( null == classRel ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
			fRelEnemy = classRel.getField( "ENEMY" );
			if ( null == fRelEnemy ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
			enumRelEnemy = fRelEnemy.get( classRel );
			if ( null == enumRelEnemy ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			failed=true;
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed=true;
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
			failed=true;
		} catch ( NoSuchFieldException e ) {
			e.printStackTrace();
			failed=true;
		} catch ( SecurityException e ) {
			e.printStackTrace();
			failed=true;
		} catch ( NoSuchMethodException e ) {
			e.printStackTrace();
			failed=true;
		} finally {
			if ( failed ) {
				throw FactionsPlus.bailOut( fpInst, "failed to hook into Factions 1.6.x" );
			}
		}
	}
	
	
//	@Override
//	public void setFlag( Faction forFaction, FFlag whichFlag, Boolean whatState ) {
//		throw FactionsPlus.bailOut( fpInst, "plugin author forgot to override w/o calling super() on this method");
//	}
	
	
	@Override
	public FactionsAny.Relation getRelationTo( FPlayer one, FPlayer two ) {
		boolean failed = false;
		FactionsAny.Relation ret = null;
		try {
			if ( ( null == one ) || ( null == two ) ) {
				failed = true;
				return null;// not gonna happen really
			}
			
			Object isReturn = mGetRelationTo.invoke( one, two );
			if ( enumRelEnemy.equals( isReturn ) ) {
				ret = FactionsAny.Relation.ENEMY;
			} else {
				// add more flags here
				failed = true;
				throw FactionsPlus
					.bailOut( fpInst, "the plugin author forgot to define any more flags for " + isReturn );
			}
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
			if ( ( failed ) || ( null == ret ) ) {
				throw FactionsPlus.bailOut( fpInst, "failed to invoke " + mGetRelationTo );
			}
		}
		return ret;// not reached
	}


	@Override
	public void addSubCommand( FCommand subCommand ) {
		P.p.cmdBase.addSubCommand(subCommand);//practically all that we need to do for 1.7 to have the commands in help
	}
}
