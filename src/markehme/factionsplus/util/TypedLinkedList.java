package markehme.factionsplus.util;

import java.util.Iterator;
import java.util.LinkedList;



public class TypedLinkedList<TYPE> implements Iterable<TYPE> {
	
	private LinkedList<TYPE>	ll	= new LinkedList<TYPE>();
	
	
	public int indexOf( TYPE obj ) {
		return ll.indexOf( obj );
	}
	
	public void addFirst(TYPE first) {
		ll.addFirst( first );
	}
	
	public void addLast( TYPE obj ) {
		ll.addLast( obj );
	}
	
	
	public boolean contains( TYPE obj ) {
		return ll.contains( obj );
	}
	
	public void clear() {
		ll.clear();
	}
	
	public TYPE getFirst() {
		return ll.getFirst();
	}
	
	public TYPE removeFirst() {
		return ll.removeFirst();
	}
	
	public boolean remove(TYPE existing) {
		return ll.remove( existing );
	}
	
	public TYPE getOriginal( TYPE scanner ) {
		int i = indexOf( scanner );
		if ( i < 0 ) {
			return null;
		} else {
			return get( i );
		}
	}
	
	
	public TYPE get( int index ) {
		return ll.get( index );
	}
	
	
	@Override
	public Iterator<TYPE> iterator() {
		return ll.iterator();
	}
	
	
	public int size() {
		return ll.size();
	}
}
