package me.markeh.factionsframework.command.versions;

import java.util.ArrayList;
import java.util.HashMap;

import com.massivecraft.factions.P;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.objs.NotifyEvent;

public class FactionsCommandManager1_6UUID extends FactionsCommandManager {
	
	// ------------------------------
	//  Fields
	// ------------------------------
	
	// Wrappers
	private HashMap<FactionsCommand, FactionsCommand1_6UUIDWrapper> wrappers = new HashMap<FactionsCommand, FactionsCommand1_6UUIDWrapper>();
	
	// Backup of help pages
	private ArrayList<ArrayList<String>> pagesBackup;
	
	// Help lines (1.6 requires adding manually) 
	private HashMap<FactionsCommand, String> helpLine = new HashMap<FactionsCommand, String>();
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	@Override
	public void addCommand(FactionsCommand cmd) {	
		wrappers.put(cmd, new FactionsCommand1_6UUIDWrapper(cmd, cmd.aliases, cmd.requiredArguments, cmd.optionalArguments));
		P.p.cmdBase.addSubCommand(wrappers.get(cmd));
		
		helpLine.put(cmd, cmd.helpLine);		
	}
	
	@Override
	public void removeCommand(FactionsCommand cmd) {
		P.p.cmdBase.subCommands.remove(wrappers.get(cmd));
		
		wrappers.remove(cmd);
		helpLine.remove(cmd);
		
		rebuldPages();
	}
	
	@Override
	public void notify(NotifyEvent event) {
		if(event == NotifyEvent.Loaded) {
			rebuldPages();
		}
		
		if(event == NotifyEvent.Stopping) {
			revertPages();
			pagesBackup = null;
		}
	}
	
	// Revert the pages back to normal 
	public void revertPages() {
		if (pagesBackup != null) { 
			P.p.cmdBase.cmdHelp.helpPages.retainAll(new ArrayList<>());
			P.p.cmdBase.cmdHelp.helpPages.addAll(pagesBackup);
		}
	}
	
	// Rebuild our pages 
	public void rebuldPages() {
		
		if(P.p.cmdBase.cmdHelp.helpPages == null) {
			P.p.cmdBase.cmdHelp.updateHelp();
		}
		
		if(pagesBackup == null) {
			pagesBackup = new ArrayList<ArrayList<String>>();
						
			for(ArrayList<String> page : P.p.cmdBase.cmdHelp.helpPages) {
				ArrayList<String> clonedPage = new ArrayList<String>();
				
				for(String line : page) {
					clonedPage.add(line);
				}
				
				pagesBackup.add(clonedPage);
			}
		}
		
		revertPages();
		
		int linesPerPage = 5; // coke-free
		
		ArrayList<String> lines = new ArrayList<String>();
		
		for (String line : helpLine.values()) {
			lines.add(line);
						
			if (lines.size() >= linesPerPage) {
				P.p.cmdBase.cmdHelp.helpPages.add(lines);
				lines.clear();
			}
		}
		
		if (lines.size() > 0) {
			P.p.cmdBase.cmdHelp.helpPages.add(lines);
			lines.clear();
		}		
	}
}
