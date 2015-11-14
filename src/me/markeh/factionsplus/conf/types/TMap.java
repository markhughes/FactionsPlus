package me.markeh.factionsplus.conf.types;

import java.util.Collection;
import java.util.HashMap;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.obj.TType;

public class TMap<K, V> extends TType<TMap<?, ?>> {
	
	public static TMap<?, ?> fromRaw(String raw) {
		String[] rows = raw.split("',");
		
		for(String row : rows) {
			FactionsPlus.get().log(row);
		}
		
		return null;
	}

	private HashMap<K, V> hashMap = new HashMap<K, V>();
		
	@Override
	public String asRawString() {
		Boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		
		for(K key : this.hashMap.keySet()) {
			if(isFirst) {
				isFirst = false;
			} else {
				sb.append(", ");
			}
			
			String value = "";
			
			if (this.hashMap.get(key) instanceof TType) {
				value = "'" + ((TType<?>) this.hashMap.get(key)).asRawString().replaceAll("'", "\'") + "'";
			} else {
				value = "'" + this.hashMap.get(key).toString().replaceAll("'", "\'") + "'";
			}
						
			sb.append("'"+ key.toString().replace("'", "\'") + "' => " + value);
		}
		
		return sb.toString();
	}

	@Override
	public TMap<K, V> valueOf(String raw) {
		/*
		 
		for(String key : this.hashMap.keySet()) {
			if(isFirst) {
				isFirst = false;
			} else {
				sb.append(", ");
			}
			
			String value = "'" + this.hashMap.get(key).toString().replaceAll("'", "\'") + "'";
			
			sb.append("'"+ key.replaceAll("'", "\'") + "' => " + value);
		}
		
		 */
		
		// TODO: convert this string back into a delicious map
		return null;
	}
	
	// Add methods 
	public void add(K key, V value) {
		this.hashMap.put(key, value);
	}
	
	// Remove methods 
	public void remove(K key) {
		this.hashMap.remove(key);
	}
		
	// Contains methods
	public boolean containsKey(K key) {
		return this.hashMap.containsKey(key);
	}
	
	// Get method
	public V get(String key) {
		return this.hashMap.get(key);
	}
	
	public boolean isEmpty() {
		return this.hashMap.isEmpty();
	}
	
	public Collection<K> getKeys() {
		return this.hashMap.keySet();
	}
	
	public Collection<V> getValues() {
		return this.hashMap.values();
	}
	
	public String findKeyLike(K key) {
		if(key instanceof String) {
			String keyString = (String) key;
			
			int i = 1;
			
			while(i < 4) {
				if(keyString.length()-i <= 0) {
					i = 4; 
					continue;
				}
				
				String lookingFor = keyString.substring(0, keyString.length()-i);
								
				for (K n : this.getKeys()) {
					String nStr = (String) n;
					if(nStr.startsWith(lookingFor)) {
						return nStr;
					}
				}
				
				i++;
			}
			
			return null;
		}
		
		return null;
	}

}
