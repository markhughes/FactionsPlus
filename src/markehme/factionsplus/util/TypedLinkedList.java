package markehme.factionsplus.util;

import java.util.*;

import markehme.factionsplus.config.*;


public final class TypedLinkedList<TYPE> implements Iterable<TYPE>{
	private LinkedList<TYPE> ll=new LinkedList<TYPE>();
	
	public int indexOf(TYPE obj){
		return ll.indexOf( obj );
	}

	public void addLast( TYPE obj ) {
		ll.addLast( obj );
	}

	public boolean contains( TYPE obj ) {
		return ll.contains( obj );
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
