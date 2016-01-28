package me.markeh.factionsplus.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Chunk;
import org.bukkit.util.Vector;

public class Utils {
	private static Utils instance;
	public static Utils get() {
		if (instance == null) instance = new Utils();
		
		return instance;
	}
	
	public String MD5(String msg) {
		if (msg == null) return null;
		
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return msg;
		}
		
        md.update(msg.getBytes());
        
        byte byteData[] = md.digest();
 
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
	}
	
	public List<String> getList(String... values) {
		List<String> list = new ArrayList<String>();
		
		for (String value : values) {
			list.add(value);
		}
		
		return list;
	}
	
	public <K,V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
		
		Collections.sort(sortedEntries, 
			new Comparator<Entry<K,V>>() {
				@Override
				public int compare(Entry<K,V> e1, Entry<K,V> e2) {
					return e2.getValue().compareTo(e1.getValue());
				}
			}
		);
		
		return sortedEntries;
	}
	
	public Vector getChunkMinVector(Chunk chunk) {
		int minX = chunk.getX() * 16;
		int minZ = chunk.getZ() * 16;
		return new Vector(minX, 0, minZ);
	}
	
	public Vector getChunkMaxVector(Chunk chunk) {
		int minX = chunk.getX() * 16;
		int minZ = chunk.getZ() * 16;
		int maxX = minX + 15;
		int maxZ = minZ + 15;
		return new Vector(maxX, chunk.getWorld().getMaxHeight(), maxZ);
	}
}
