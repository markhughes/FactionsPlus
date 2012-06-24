package markehme.factionsplus.extras;

import java.util.*;



/**
 * maps/links a tuple of two non-null instances<br>
 * it will allow looking up by either the key or the value<br>
 * know one and you can get the other<br>
 * 
 * @param <K> cannot be null
 * @param <V> cannot be null
 */
public class TwoWayHashMapOfNonNulls<K, V> {//beware of some paranoid programming below hehe, for fun:)
	
	private AbstractMap<K, V>	mapForward	= new HashMap<>();
	private AbstractMap<V, K>	mapBackward	= new HashMap<>();
	
	private final static void nn(Object toTest) {
		if (null == toTest) {
			throw new NullPointerException("a key or value was null at this point");
		}
	}
	public TwoWayHashMapOfNonNulls() {
		// nothing here
	}
	
	
	public V getValue( K byKey ) {
		nn(byKey);
		V ret = mapForward.get( byKey );
		nn(ret);
		return ret;
	}
	
	
	public K getKey( V byValue ) {
		nn(byValue);
		K ret = mapBackward.get( byValue );
		nn(ret);
		return ret;
	}
	
	
	public boolean putKeyValue( K key, V value ) {
		nn(key);
		nn(value);
		boolean existed1=mapForward.containsKey( key );
		boolean existed2=mapBackward.containsKey( value );
		V prevVal = mapForward.put( key, value );
		K prevKey = mapBackward.put(value, key);
		if ((null != prevVal) ^ (null != prevKey)) {
			//inconsistency detected somehow
			// 0 xor 0 = 0
			// 1 xor 0 = 1
			// 0 xor 1 = 1
			// 1 xor 1 = 0
			// "^" is "xor"
			// so if both were 1 or both were 0 => 0
			//which means we wouldn't be here
			//since we're here it means, one of our maps had a connection which the other didn't
			//which would indicate implementation failure somewhere in the coding on this class
			//tmi?
			throw new RuntimeException("implementation failure");
		}
		boolean existed=(null != prevVal) || (null != prevKey);
		return existed;//since prev can never be null because we don't accept null key/value(s)
	}
}
