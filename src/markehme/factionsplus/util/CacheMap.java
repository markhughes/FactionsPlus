package markehme.factionsplus.util;

import java.util.*;

import org.bukkit.*;

import com.massivecraft.factions.*;

import markehme.factionsplus.*;
import markehme.factionsplus.util.*;


/**
 * this must be thread safe
 * @param <KEY>
 * @param <VALUE>
 */
public class CacheMap<KEY, VALUE> {
	private final HashMap<KEY,VALUE> map;
	private final QueueFIFO<KEY> queueFIFO;//to know which was the oldest inserted one
	private final int max;
	
	public CacheMap(int maxInCache){
		max=maxInCache;
		assert max>0;
		map=new HashMap<KEY, VALUE>( max);
		queueFIFO=new QueueFIFO<KEY>();
	}
	
	/**
	 * add new (will throw if existed already(only if -ea vmarg), 
	 * it's up to you to use get prior to this call to see if it existed)<br>
	 * @param key
	 * @param val
	 * @return previous value, if key existed already
	 */
	public synchronized VALUE put(KEY key, VALUE val) {
		assert Q.nn( key );
		assert Q.nn( val );
		VALUE prevVal = map.put( key, val );
		assert null == prevVal:"already existed";
		if (queueFIFO.size()>=max) {
			KEY removedKey = queueFIFO.removeLast();
			assert null != removedKey;
		}
		queueFIFO.addFirst( key );
		return prevVal;//can be null
	}

	public synchronized VALUE get( KEY key ) {
		assert Q.nn(key);
		VALUE val = map.get( key );
		//maybe reorder this as first, since it could've been the last one, but yet it's being used
		//and thus prevent it from getting removed if a new item is inserted
		queueFIFO.moveFirst( key );
		return val;
	}

	public synchronized VALUE remove( KEY id ) {
		VALUE prev = map.remove( id );
		boolean existed = queueFIFO.remove(id);
		assert existed == (null != prev);
		return prev;
	}
	
}
