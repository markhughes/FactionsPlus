package me.markeh.factionsplus.conf.types;

import java.util.Collection;
import java.util.HashMap;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.obj.TType;

public class TMap<K, V> extends TType<TMap<?, ?>> {
	
	public static TMap<?, ?> fromRaw(String raw) {
		String[] rows = raw.split("',");
		
		FactionsPlus.get().debug("Rows:");
		
		Class<?> keyType = null;
		Class<?> valueType = null;
		
		TMap<Object, Object> newTMap = new TMap<Object, Object>();
		
		Boolean isFirst = true;
		
		for(String row : rows) {
			row = row.trim();
			
			if (row.isEmpty() || row.equalsIgnoreCase("'")) continue;
			
			if ( ! row.endsWith("'")) row = row + "'";
			
			FactionsPlus.get().debug("row raw:" + row);
			
			String[] data = row.split("' => '");
			
			if (data.length == 0) continue;
			
			String key = data[0];
			
			if (isFirst) {
				isFirst = false;
				
				String[] data2 = key.split(" ");
				String[] data3 = data2[0].split(":");
				
				try {
					keyType = Class.forName(data3[0]);
					valueType = Class.forName(data3[1]);
				} catch (ClassNotFoundException e) {
					FactionsPlus.get().logError(e);
					return newTMap;
				}
				
				key = data2[1];
			}
			key = key.substring(1);
			
			// use raw value 
			String rawValue = data[1].substring(0, data[1].length()-1);
			
			newTMap.add(buildObject(keyType, key), buildObject(valueType, rawValue));
			FactionsPlus.get().debug("key ("+keyType.toGenericString()+") = " + key);
			FactionsPlus.get().debug("raw value ("+valueType.toGenericString()+") = " + rawValue);
		}
		
		FactionsPlus.get().debug("");
		
		return newTMap;
	}
	
	private HashMap<K, V> hashMap = new HashMap<K, V>();
		
	@Override
	public String asRawString() {
		// if the keys are empty then don't bother 
		if (this.getKeys().size() == 0 || this.getValues().size() == 0) return "";

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
				if (this.hashMap.get(key) != null) value = "'" + this.hashMap.get(key).toString().replaceAll("'", "\'") + "'";
				else value = "''";
			}
						
			sb.append("'"+ key.toString().replace("'", "\'") + "' => " + value);
		}
		
		try {
			return this.getKeys().toArray()[0].getClass().getName() + ":" + this.getValues().toArray()[0].getClass().getName() + " " + sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	@Deprecated
	public TMap<K, V> valueOf(String raw) {
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
	
	private static Object buildObject(Class<?> type, String rawValue) {
		if (type == TLoc.class) {
			return TLoc.fromRaw(rawValue);
		} else if(type == Double.class) {
			return Double.valueOf(rawValue);
		} else if(type == Integer.class) {
			return Integer.valueOf(rawValue);
		} else if(type == Long.class) {
			return Long.valueOf(rawValue);
		} else if(type == Boolean.class) {
			return Boolean.valueOf(rawValue);
		} else if(type == String.class) {
			return String.valueOf(rawValue);
		} else {
			// Unsupported or can take a raw form..
			return rawValue;
		}
	}

}
