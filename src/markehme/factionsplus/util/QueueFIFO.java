package markehme.factionsplus.util;

import java.util.*;



/**
 * non-null elements only<br>
 * not thread safe, must be externally sync-ed
 * 
 * @param <ELEMENT>
 */
public class QueueFIFO<ELEMENT> {
	
	private LinkedList<ELEMENT>	list	= new LinkedList<ELEMENT>();
	
	
	/**
	 * @param whichElement
	 *            moves this to first position
	 */
	public void moveFirst( ELEMENT whichElement ) {
		assert Q.nn( whichElement );
		int index = list.indexOf( whichElement );
		if ( index > 1 ) {// aka avoid not found or already first
			ELEMENT prev = list.remove( index );
			assert null != prev;
			assert prev == whichElement : " they were not ref equal, are they .equals? =" + whichElement.equals( prev );
			list.addFirst( prev );
		}
	}
	
	
	public int size() {
		return list.size();
	}
	
	
	public void addFirst( ELEMENT elem ) {
		assert Q.nn( elem );
		list.addFirst( elem );
	}
	
	
	public ELEMENT removeLast() {
		return list.removeLast();
	}
	
	public boolean remove(ELEMENT elem) {
		return list.remove( elem );
	}
}
