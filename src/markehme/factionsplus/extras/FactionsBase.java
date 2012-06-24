package markehme.factionsplus.extras;

import java.lang.reflect.*;

import markehme.factionsplus.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;



public abstract class FactionsBase implements FactionsAny {
	
	protected FactionsPlus	fpInst;
	private Class			classRP			= null;//the class/interface RelationParticipator
	private Class			classRel		= null;//the class or enum class Rel or Relation
	private Field			fRelEnemy		= null;//the ENEMY as field not instance
	private Object			enumRelEnemy	= null;//Rel.ENEMY or Relation.ENEMY (as instance well, as enum really)
	private Method			mGetRelationTo	= null;//the method getRelationTo(RelationParticipator rp)
	
	
	protected FactionsBase( FactionsPlus fpInstance ) {
		boolean failed = false;
		fpInst = fpInstance;
		assert null != fpInst;
		
		try {
			classRP = Class.forName( "com.massivecraft.factions.iface.RelationParticipator" );
			
			mGetRelationTo = FPlayer.class.getMethod( "getRelationTo", classRP );
			
			classRel =
				Class.forName( "com.massivecraft.factions.struct."
					+ ( Factions16.class.equals( this.getClass() ) ? "Relation" : "Rel" ) );
			
			fRelEnemy = classRel.getField( "ENEMY" );
			
			enumRelEnemy = fRelEnemy.get( classRel );//safe to get here this soon;
			if ( null == enumRelEnemy ) {// this is likely never null, it would rather just throw instead
				failed = true;
				return;
			}
			
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( NoSuchFieldException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( SecurityException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( NoSuchMethodException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlus.bailOut( fpInst, "failed to hook into Factions 1.6.x" );
			}
		}
	}
	
	
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
		return ret;// actually reached
	}
	
	
	@Override
	public void addSubCommand( FCommand subCommand ) {
		P.p.cmdBase.addSubCommand( subCommand );// practically all that we need to do for 1.7 to have the commands in help
	}
}
