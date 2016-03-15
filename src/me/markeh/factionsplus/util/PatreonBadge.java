package me.markeh.factionsplus.util;

import java.io.File;
import java.util.Scanner;

import me.markeh.factionsplus.FactionsPlus;

public class PatreonBadge {
	
	private static PatreonBadge instance = null;
	public static PatreonBadge get() {
		if (instance == null) instance = new PatreonBadge();
		return instance;
	}
	
	private boolean isPatreon = false;
	private String name = "";
	
	public final void setup() {
		File patreonFile = new File(FactionsPlus.get().getDataFolder(), "patreon.key");
		
		if ( ! patreonFile.exists()) return;
		
		try {
			@SuppressWarnings("resource")
			String data = new Scanner(patreonFile).useDelimiter("\\Z").next();
			
			String[] datas = data.split("::");
			String validator = Utils.get().MD5("MMPatreon" + datas[0]).toUpperCase();
						
			if ( ! validator.equalsIgnoreCase(datas[1])) return;
			
			this.isPatreon = true;
			this.name = datas[0];
			
		} catch (Exception e) { }
	}
	
	public final void display() {
		if (this.isPatreon) {
			FactionsPlus.get().log("<green>Thanks " + this.name + " for your Patreon pledge!");
		} else {
			FactionsPlus.get().log("<red>             ------------------------------------------");
			FactionsPlus.get().log("<white><bold>               Consider supporting my patreon campaign! ");
			FactionsPlus.get().log("<green>                <underline>https://www.patreon.com/markehme");
			FactionsPlus.get().log("<red>             ------------------------------------------");

		}
	}
}
