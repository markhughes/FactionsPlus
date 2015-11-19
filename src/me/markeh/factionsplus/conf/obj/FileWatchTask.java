package me.markeh.factionsplus.conf.obj;

import java.io.File;
import java.util.TimerTask;

import me.markeh.factionsplus.FactionsPlus;

public abstract class FileWatchTask extends TimerTask {
	
	private long timeStamp;
	private File file;
	private Configuration<?> configuration;
	
	public FileWatchTask(File file, Configuration<?> configuration) {
		this.file = file;
		this.timeStamp = file.lastModified();
		this.configuration = configuration;
	}
	
	public final void run() {
		long fileTimeStamp = this.file.lastModified();
		
		if (this.timeStamp == fileTimeStamp) return;
		
		this.timeStamp = fileTimeStamp;
		onChange(this.file, configuration);
		
		FactionsPlus.get()
			.debug("<green>Configuration Watch Task Completed: <?> != <?>", String.valueOf(this.timeStamp), String.valueOf(fileTimeStamp))
			.debug("<green><?>", this.file.getAbsolutePath().toString());
	}
	
	protected abstract void onChange(File file, Configuration<?> configuration);
	
}
