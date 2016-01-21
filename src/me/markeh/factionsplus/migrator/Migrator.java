package me.markeh.factionsplus.migrator;

import java.util.ArrayList;
import java.util.List;

import me.markeh.factionsplus.migrator.fdata.MigrateFData;

public class Migrator {
	
	public Migrator() {
		this.addMigrator(new MigrateFData());
	}
	
	public List<MigrationRunner> migrators = new ArrayList<MigrationRunner>();
	
	public void addMigrator(MigrationRunner migrator) {
		this.migrators.add(migrator);
	}
	
	public void execute() {
		for (MigrationRunner runner : this.migrators) {
			runner.run();
		}
	}
}
