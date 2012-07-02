package markehme.factionsplus.util;

import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.extras.*;
import markehme.factionsplus.util.*;


/**
 * maps/links a tuple of two non-null instances<br>
 * it will allow looking up by either the key or the value<br>
 * know one and you can get the other<br>
 * 
 * @param <LEFTSIDE>
 *            cannot be null
 *            must be unique when compared to other elements on this side
 * @param <RIGHTSIDE>
 *            cannot be null
 *            must be unique when compared to other elements on this side
 */
public class TwoWayMapOfNonNulls<LEFTSIDE, RIGHTSIDE> implements Map<LEFTSIDE, RIGHTSIDE> {// beware of some paranoid
																							// programming below hehe, for fun:)

	private AbstractMap<LEFTSIDE, RIGHTSIDE>	mapLRForward	= new HashMap<LEFTSIDE, RIGHTSIDE>();
	private AbstractMap<RIGHTSIDE, LEFTSIDE>	mapRLBackward	= new HashMap<RIGHTSIDE, LEFTSIDE>();
	
	
	private final static void nn( Object toTest ) {
		if ( null == toTest ) {
			throw new NullPointerException( "a key or value was null at this point" );
		}
	}
	
	
	public TwoWayMapOfNonNulls() {
		// nothing here
	}
	
	
	/**
	 * @param sideLeft
	 * @return null if not found
	 */
	public RIGHTSIDE getRightSide( LEFTSIDE sideLeft ) {
		nn( sideLeft );
		RIGHTSIDE ret = mapLRForward.get( sideLeft );
		return ret;
	}
	
	
	/**
	 * @param sideRight
	 * @return null if not found
	 */
	public LEFTSIDE getLeftSide( RIGHTSIDE sideRight ) {
		nn( sideRight );
		LEFTSIDE ret = mapRLBackward.get( sideRight );
		return ret;
	}
	
	
	public boolean putLR( LEFTSIDE sideLeft, RIGHTSIDE sideRight ) {
		nn( sideLeft );
		nn( sideRight );
//		boolean existed1 = mapLRForward.containsKey( sideLeft );
//		boolean existed2 = mapRLBackward.containsKey( sideRight );
		RIGHTSIDE prevVal = mapLRForward.put( sideLeft, sideRight );
		LEFTSIDE prevKey = mapRLBackward.put( sideRight, sideLeft );
		if ( ( null != prevVal ) ^ ( null != prevKey ) ) {
			// inconsistency detected somehow
			// 0 xor 0 = 0
			// 1 xor 0 = 1
			// 0 xor 1 = 1
			// 1 xor 1 = 0
			// "^" is "xor"
			// so if both were 1 or both were 0 => 0
			// which means we wouldn't be here
			// since we're here it means, one of our maps had a connection which the other didn't
			// which would indicate implementation failure somewhere in the coding on this class
			// tmi?
			throw new RuntimeException( "implementation failure, wanted to put: `"+sideLeft+
				"`->`"+sideRight+"` but this part existed already: `"+ (prevKey!=null?sideRight:sideLeft) +"`");
		}
		boolean existed = ( null != prevVal ) || ( null != prevKey );
		return existed;// since prev can never be null because we don't accept null key/value(s)
	}
	
	
	@Override
	public int size() {
		return mapLRForward.size();
	}
	
	
	@Override
	public boolean isEmpty() {
		return mapLRForward.isEmpty();
	}
	
	
	@Override
	public boolean containsKey( Object key ) {
		nn(key);
		return mapLRForward.containsKey( key );
	}
	
	
	@Override
	public boolean containsValue( Object value ) {
		nn(value);
		return mapRLBackward.containsKey( value );
	}
	
	
	@Override
	public RIGHTSIDE get( Object key ) {
		return getRightSide( (LEFTSIDE)key );
	}
	
	
	@Override
	public RIGHTSIDE put( LEFTSIDE key, RIGHTSIDE value ) {
//		nn(key);
//		nn(value);
		RIGHTSIDE prevR = getRightSide( key );
		putLR( key, value );
		return prevR;
	}
	
	
	@Override
	public RIGHTSIDE remove( Object key ) {
		nn(key);
		RIGHTSIDE prevR = mapLRForward.remove( key );
		if ( null != prevR ) {
			mapRLBackward.remove( prevR );
		}
		return prevR;
	}
	
	
	@Override
	public void putAll( Map<? extends LEFTSIDE, ? extends RIGHTSIDE> m ) {
		// TODO Auto-generated method stub
		throw Q.ni();
	}
	
	
	@Override
	public void clear() {
		mapLRForward.clear();
		mapRLBackward.clear();
	}
	
	
	@Override
	public Set<LEFTSIDE> keySet() {
		// TODO Auto-generated method stub
		throw Q.ni();
	}
	
	
	@Override
	public Collection<RIGHTSIDE> values() {
		// TODO Auto-generated method stub
		throw Q.ni();
	}
	
	
	@Override
	public Set<java.util.Map.Entry<LEFTSIDE, RIGHTSIDE>> entrySet() {
		// TODO Auto-generated method stub
		throw Q.ni();
	}
}
