package me.markeh.factionsplus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	
}
