package markehme.factionsplus.Cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Cmds.req.ReqFactionsEconomyEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.Txt;

public class CmdMoneyTop extends FPCommand implements Runnable {
	public CmdMoneyTop() {
		this.aliases.add("top");
		
		this.fpidentifier = "top";
		
		this.optionalArgs.put("page", "1");
		this.errorOnToManyArgs = true;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqFactionsEconomyEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.MONETOP.node));
		
		this.setHelp(LConf.get().cmdDescMoneyTop);
		this.setDesc(LConf.get().cmdDescMoneyTop);
	}
	
	private static HashMap<String, ArrayList<String>> pages = new HashMap<String, ArrayList<String>>();
	
	private static List<UPlayer> toNotify = new ArrayList<UPlayer>();
	
	private static Boolean sorting = false;
	
	private static volatile int	taskId = Integer.MIN_VALUE;
	
	private static CmdMoneyTop sorter = null;
	
	@Override
	public void performfp() {
		if(!pages.containsKey(universe)) {
			pages.put(universe, new ArrayList<String>());
		}
		
		if(pages.get(universe).size() > 0) {
			sendPage(usender, args);
		} else {
			if(sorting) {
				msg(Txt.parse(LConf.get().moneyTopCurrentlySorting));
				
				if(!toNotify.contains(usender)) {
					toNotify.add(usender);
				}
			} else {
				sortFactionBanks();
			}
		}
	}
	
	public void sendPage(UPlayer usender, List<String> args) {
		int page = 1;
		
		if(args.size() != 0 && args != null) {
			try {
				page = Integer.parseInt(args.get(0));
			} catch (Throwable t) {
				// Simply ignore 
			}
		}
		
		if(page < 1) page = 1;
		
		int startAt = page*5-5;
		int finishAt = startAt+5;
		
		int count = startAt;
		
		// Clone to be safe! 
		ArrayList<String> ps = (ArrayList<String>) pages.get(usender.getUniverse());
		
		while(count != finishAt) {
			if(!ps.contains(startAt+count)) {
				break;
			}
			usender.msg(ps.get(startAt+count));
			
			count++;
		}
		
		usender.msg(ChatColor.BOLD + "Page: " + page + "/" + Math.floor((pages.size()/5)));
		
	}
	
	public static boolean isRunningSorter() {
		synchronized(CmdMoneyTop.class) {
			return(taskId >= 0) && (sorter != null);
		}
	}
	
	public void sortFactionBanks() {
		if(!isRunningSorter()) {
			if(taskId < 0) {
				if(sorter == null) sorter = new CmdMoneyTop();
				
				taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(FactionsPlus.instance, sorter, 1, 60*5*20);
				
				if(taskId < 0) {
					FactionsPlus.warn("Failed to start a thread to sort top factions by money");
				}

			}
		}
	}
	
	@Override
	public void run() {
		synchronized(CmdMoneyTop.class) {
			sorting = true;
			
			for(FactionColl fc : FactionColls.get().getColls()) {
				HashMap<Double, Faction> factionsMap = new HashMap<Double, Faction>();
				
				for(Faction f : fc.getAll()) {
					factionsMap.put(Money.get(f), f);
				}
				
				// Check if the pages contains this universe 
				if(!pages.containsKey(fc.getUniverse())) {
					pages.put(fc.getUniverse(), new ArrayList<String>());
				} else {
					// It exists, clear it out
					pages.get(fc.getUniverse()).clear();
				}
				
				// Sort the HashMap
				List<Double> keys = new LinkedList<Double>(factionsMap.keySet());
				Collections.sort(keys);
				Map<Double, Faction> sortedMap = new LinkedHashMap<Double, Faction>();
				for(Double key: keys) {
					pages.get(fc.getUniverse()).add(Txt.parse(LConf.get().moneyTopPagesStyle, key, sortedMap.get(key)));
				}
			}
			
			// Sorting is done
			sorting = false;
			
			if(toNotify.size() > 0) {
				for(Object objectPlayer : toNotify.toArray()) {
					UPlayer usender = (UPlayer) objectPlayer;
					if(usender != null) {
						if(usender.isOnline()) {
							sendPage(usender, null);
						}
					}
				}
			}
		}
	}
	
}
