package markehme.factionsplus.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.MConf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.Txt;

/**
 * TODO:
 *  - Land info on entry
 *  - MarkedMap
 */
public class ScoreboardManager implements Runnable {
	
	private String scoreboard_prefix = "FPSB_";
	
	private int taskID = -1;
	
	private FactionsPlus instance;
	
	private boolean isSorting = false;
	
	/**
	 * Collection of scoreboards, per universe:
	 * -- universe
	 * -- -- infotype (power, members, etc)
	 */
	private HashMap<String, HashMap<CurrentScoreboard, Scoreboard>> scoreboards = new HashMap<String, HashMap<CurrentScoreboard, Scoreboard>>();
	
	private HashMap<Player, CurrentScoreboard> playerSteps = new HashMap<Player, CurrentScoreboard>();
	
	public ScoreboardManager(FactionsPlus instance) {
		this.instance = instance;
	}
	
	/**
	 * Returns the prefix used by scoreboard identifiers 
	 * @return Prefix
	 */
	public String getPrefix() {
		return scoreboard_prefix;
	}
	
	/**
	 * Determines if the Scoreboard task is running
	 * @return
	 */
	public boolean isRunning() {
		return(taskID > 0);
	}
	
	/**
	 * Starts the Scoreboard task
	 */
	public void start() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, this, 0, MConf.get().scoreboardUpdate*20L);
	}
	
	/**
	 * Stops the Scoreboard task
	 */
	public void stop() {
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
	public void apply(Player player) {
		synchronized(ScoreboardManager.class) {
			String universe = Factions.get().getMultiverse().getUniverseForWorldName(player.getWorld().getName());
			final FPUConf fpuconf = FPUConf.get(player);
			
			if(Factions.get().getMultiverse().getUniverseForWorldName(universe) != null) {
				if(UConf.get(universe).enabled) {
					if(!playerSteps.containsKey(player)) {
						playerSteps.put(player, null);
					}
					
					if(fpuconf.scoreboardRotateEnabled) {
						CurrentScoreboard next = playerSteps.get(player).getNext(fpuconf);
						
						playerSteps.remove(player);
						playerSteps.put(player, next);
						
						player.setScoreboard(scoreboards.get(universe).get(next));
						
						
					} else {
						player.setScoreboard(scoreboards.get(universe).get(fpuconf.scoreboardDefault));
					}
					
					
					return;
				}
			}
			
			for(Objective o : player.getScoreboard().getObjectives()) {
				if(o.getName().startsWith(scoreboard_prefix)) {
					player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				}
			}
		}
	}
	
	public void clean() {
		for(String u : scoreboards.keySet()) {
			for(CurrentScoreboard o : scoreboards.get(u).keySet()) {
				scoreboards.get(u).get(o).getObjectives().clear();
			}
		}
		
		scoreboards.clear();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		synchronized(ScoreboardManager.class) {
			boolean keeprunning = false;
			
			// Run through the worlds in the multiverse with the aspect of factions, and
			// check if there are plaers on it.
			for(String w : Factions.get().getMultiverse().getWorlds()) {
				if(Bukkit.getWorld(w).getPlayers().size() > 0) {
					keeprunning = true;
				}
			}
			
			
			if(!keeprunning) {
				// If there are no players online, stop the task - it can start again when players
				// are online. 
				stop();
				return;
			}
			
			// Ensure we aren't already sorting data
			if(isSorting) {
				return;
			}
			
			isSorting = true;
			
			for(String universe : Factions.get().getMultiverse().getUniverses()) {
				HashMap<Faction, Double> power		= new HashMap<Faction, Double>();
				HashMap<Faction, Double> money		= new HashMap<Faction, Double>();
				HashMap<Faction, Integer> members	= new HashMap<Faction, Integer>();
				HashMap<Faction, Integer> land 		= new HashMap<Faction, Integer>();
				
				// Stage 1: Grab information and store in hashmap
				for(Faction f : FactionColls.get().getForUniverse(universe).getAll()) {
					// infotype 1: power
					power.put(f, f.getPower());
					
					// infotype 2: money
					money.put(f, Money.get(f));
					
					// infotype 3: members
					members.put(f, f.getUPlayers().size());
					
					// infotype 4: land owned
					land.put(f, f.getLandCount());
				}
				
				// Sort all the information (warning supression ignores these being unchecked)
				power = sortHashMapByValuesD(power);
				money = sortHashMapByValuesD(money);
				members = sortHashMapByValuesD(members);
				land = sortHashMapByValuesD(land);	
				
				if(!scoreboards.containsKey(universe)) {
					scoreboards.put(universe, new HashMap<CurrentScoreboard, Scoreboard>());
				}
				
				// Set up our scoreboards
				
				Scoreboard scoreboardPower = null;
				Scoreboard scoreboardMoney = null;
				Scoreboard scoreboardMembers = null;
				Scoreboard scoreboardLand = null;
				
				Objective objectivePower = null;
				Objective objectiveMoney = null;
				Objective objectiveMembers = null;
				Objective objectiveLand = null;
				
				if(scoreboards.get(universe).containsKey("power")) {
					scoreboardPower = scoreboards.get(universe).get("power");
					objectivePower = scoreboardPower.getObjective(scoreboard_prefix + "power");
				} else {
					scoreboardPower = Bukkit.getScoreboardManager().getNewScoreboard();
					
					objectivePower = scoreboardPower.registerNewObjective(scoreboard_prefix + "power", "dummy");
					
					objectivePower.setDisplaySlot(DisplaySlot.SIDEBAR);

				}
				
				objectivePower.setDisplayName(Txt.parse(LConf.get().scoreboardTopPowerTitle));
				if(power.size() > 0) {
					int count = 1;
					for(Faction f : power.keySet()) {
						if(count > 10) break;
						count++;
						
						objectivePower.getScore(f.getName()).setScore((power.get(f).intValue()));
						power.get(f);
					}
				}
				
				if(scoreboards.get(universe).containsKey("money")) {
					scoreboardMoney = scoreboards.get(universe).get("money");
					objectiveMoney = scoreboardPower.getObjective(scoreboard_prefix + "money");
				} else {
					scoreboardMoney = Bukkit.getScoreboardManager().getNewScoreboard();
						
					objectiveMoney = scoreboardMoney.registerNewObjective(scoreboard_prefix + "money", "dummy");
					
					objectiveMoney.setDisplaySlot(DisplaySlot.SIDEBAR);
				}
				
				objectiveMoney.setDisplayName(Txt.parse(LConf.get().scoreboardTopMoneyTitle));
				if(money.size() > 0) {
					int count = 1;
					for(Faction f : money.keySet()) {
						if(count > 10) break;
						count++;
						
						objectivePower.getScore(f.getName()).setScore((money.get(f).intValue()));
						power.get(f);
					}
				}
				
				if(scoreboards.get(universe).containsKey("members")) {
					scoreboardMembers = scoreboards.get(universe).get("members");
					objectiveMembers = scoreboardPower.getObjective(scoreboard_prefix + "memb");
				} else {
					scoreboardMembers = Bukkit.getScoreboardManager().getNewScoreboard();
					
					objectiveMembers = scoreboardMembers.registerNewObjective(scoreboard_prefix + "memb", "dummy");
					
					objectiveMembers.setDisplaySlot(DisplaySlot.SIDEBAR);

				}
				
				objectiveMembers.setDisplayName(Txt.parse(LConf.get().scoreboardTopMembersTitle));
				if(members.size() > 0) {
					int count = 1;
					for(Faction f : members.keySet()) {
						if(count > 10) break;
						count++;
						
						objectivePower.getScore(f.getName()).setScore((members.get(f).intValue()));
						power.get(f);
					}
				}
				
				if(scoreboards.get(universe).containsKey("land")) {
					scoreboardLand = scoreboards.get(universe).get("land");
					objectiveLand = scoreboardPower.getObjective(scoreboard_prefix + "land");
				} else {
					scoreboardLand = Bukkit.getScoreboardManager().getNewScoreboard();
					
					objectiveLand = scoreboardLand.registerNewObjective(scoreboard_prefix + "memb", "dummy");
					
					objectiveLand.setDisplaySlot(DisplaySlot.SIDEBAR);
					
				}
				
				objectiveLand.setDisplayName(Txt.parse(LConf.get().scoreboardTopLandTitle));
				if(land.size() > 0) {
					int count = 1;
					for(Faction f : land.keySet()) {
						if(count > 10) break;
						count++;
						
						objectiveLand.getScore(f.getName()).setScore((members.get(f).intValue()));
						power.get(f);
					}
				}
				
			}
			
			isSorting = false;
		}
	}
	
	@SuppressWarnings("all")
	public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)){
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String)key, (Double)val);
					break;
				}

			}

		}
		return sortedMap;
	}

}
