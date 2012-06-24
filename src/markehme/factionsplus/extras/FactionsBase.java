package markehme.factionsplus.extras;

import java.lang.reflect.*;
import java.util.concurrent.*;

import markehme.factionsplus.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;



public abstract class FactionsBase implements FactionsAny {
	
	protected FactionsPlus									fpInst;
	private Class											classRP			= null; // the class/interface RelationParticipator
	private Class											classRel		= null; // the class or enum class Rel or Relation
	private Method											mGetRelationTo	= null; // the method
																					// getRelationTo(RelationParticipator rp)
																					
	// this will hold a mapping between our Factions.version independent FactionsAny.Relation and the specific Factions version
	// Relation
	// except it's mapped in reversed order
	private ConcurrentHashMap<Object, FactionsAny.Relation>	mapRelation		= null;
	
	
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
			
			mapRelation = new ConcurrentHashMap<>();
			
			for ( Field eachField : classRel.getFields() ) {
				try {
					if ( ( classRel.equals( eachField.getType() ) ) ) {
						FactionsAny.Relation ourFieldInstance =
							(Relation)( FactionsAny.Relation.class.getField( eachField.getName() )
								.get( FactionsAny.Relation.class ) );
						Object factionsFieldInstance = eachField.get( classRel );
						mapRelation.put( factionsFieldInstance, ourFieldInstance );
					}
				} catch ( IllegalArgumentException e ) {// I didn't want to catch Exception e though
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
				} finally {
					if ( failed ) {
						throw FactionsPlus.bailOut( fpInst, "the plugin author forgot to define some flags in "
							+ FactionsAny.Relation.class + " for " + eachField );
					}
				}
			}
			
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalArgumentException e ) {
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
			ret = mapRelation.get( isReturn );
			if ( null == ret ) {
				FactionsPlus.severe( "impossible" );
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
