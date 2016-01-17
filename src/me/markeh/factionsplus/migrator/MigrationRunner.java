package me.markeh.factionsplus.migrator;

import me.markeh.factionsplus.FactionsPlus;

public abstract class MigrationRunner {
	public abstract void run();
	
	public void log(String msg) {
		FactionsPlus.get().log("[Migrator] " + msg);
	}
}
