package markehme.factionsplus.Cmds;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.util.DualPack;
import markehme.factionsplus.util.RunnableWithParams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.money.Money;



public class CmdMoneyTop extends FPCommand {
	private static final int	shownPerPage	= 10;
	
	// will sort no more than once every 30 sec, but only when the command is issued
	//resorting happens again after this amount of time elapsed before the previous sort, regardless of the time elapsed between 
	//commands
	private static final long						SEC_BETWEEN_SORTS	= 30;
	private static final long						MILISEC_BETWEEN_SORTS	= SEC_BETWEEN_SORTS*1000;
	
	// how much time after last sort happened, will the sort be set to null to allow gc to clean the scoreBoard array
	//this also means, the sort will happened again if a command is issued before cleaning, even if MILISEC_BETWEEN_SORTS was not yet
	//reached
	// 2min aka 120 sec, each sec 20 ticks; currently set at 4 times the `seconds between sorts`
	//cleaning will dereference the sorted array to be subject to gc-ing and will also cause sorting to recur next time cmd is issued
	//cleaning happens only when TICKS_BEFORE_CLEAN elapsed since the last use of command
	private static final long						TICKS_BEFORE_CLEAN		= 20 * (SEC_BETWEEN_SORTS*4); 
	private static boolean							shouldClean				= false;
	private static volatile int						cleaningTaskId			= -1;
	
	public static long								lastSortTimeStamp=0;
	private static long								lastSortDuration		= 0;		// how much time the last sort operation
																						// took
																						
	private static Object[]							scoreBoard				= null;
	
	private static final Comparator<? super Object>	comparator				= new Comparator<Object>() {
																				
																				private final int compare(
																					DualPack<Faction, Double> o1,
																					DualPack<Faction, Double> o2 )
																				{
																					int x =
																						o1.getSecond().compareTo(
																							o2.getSecond() );
																					if ( x > 0 ) {
																						return +1;
																					} else
																						if ( x < 0 ) {
																							return -1;
																						} else {
																							return 0;
																						}
																				}
																				
																				
																				@Override
																				public int compare( Object o1, Object o2 ) {
																					return compare(
																						(DualPack<Faction, Double>)o2,
																						(DualPack<Faction, Double>)o1 );// reversed
																														// &
																														// casted
																														// params
																				}
																			};
	
	private static final Runnable					cleanTask				= new Runnable() {
																				
																				@SuppressWarnings( "synthetic-access" )
																				@Override
																				public void run() {
																					//don't make this blocking in any way!
																					//if you do it will lag the server
																					scoreBoard = null;// marking this as unused
																										// for gc to clean,
																										// whenever
																					cleaningTaskId=-1;
//																					System.out.println("clean task done");
																				}
																			};

	private volatile static int	sortingTaskId=-1;
	
	
	public CmdMoneyTop() {
		this.aliases.add( "top" );
		
		this.optionalArgs.put( "page", "1" );
		this.errorOnToManyArgs = true;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "show the highest ranked Factions by money" );
		this.setDesc( "show the highest ranked Factions by money" );
	
	}
	
	private static boolean	alreadyIn		= false;
	private static boolean	sorted			= false;
	
	private static final RunnableWithParams<CommandSender,  Integer>  sortingCode=
			new RunnableWithParams<CommandSender,Integer>(null){

		@SuppressWarnings( {
			"synthetic-access", "boxing"
		} )
		@Override
		public void run2( CommandSender sender) {
				
			long sortStartedAt = System.currentTimeMillis();
			// preventing resorting while in sort, if sorting time takes more than MILISEC_BETWEEN_SORTS
			lastSortTimeStamp = sortStartedAt + lastSortDuration;
			// by predicting how long this sort will take. and presuming that this command can be called again while we're
			// already in it
			mayNOTClean();
				Collection<FactionColl> all = FactionColls.get().getColls();
				setStatus(all.size());
				sender.sendMessage( "Sorting " + all.size() + " factions..." );
				scoreBoard = Arrays.copyOf( all.toArray(), all.size() );
//				try {
//					Thread.sleep(5000);//: temporary emulation
//				} catch ( InterruptedException e ) {
//					e.printStackTrace();
//				}
				
				
				for ( int i = 0; i < scoreBoard.length; i++ ) {
					Faction f = (Faction)scoreBoard[i];
					double bal = Money.get(f);
					scoreBoard[i] = new DualPack<Faction, Double>( f, bal );
				}
			
				Arrays.sort( scoreBoard, comparator );
			
				sorted=true;

			// updating these:
			lastSortTimeStamp = System.currentTimeMillis();
			lastSortDuration = lastSortTimeStamp - sortStartedAt;
			
			//after sorting is done or skipped:
			synchronized ( sortQueryLock ) {
				mayClean();
				sortingTaskId=-1;//done with this task
				
				//done: now show all the queued user->page  if any
				Set<Entry<CommandSender, Integer>> x = notifyList.entrySet();
				for ( Entry<CommandSender, Integer> iterable_element : x ) {
					showPage( iterable_element.getKey(), iterable_element.getValue().intValue() );
				}
				notifyList.clear();
			}
//			System.out.println("Sort task done");
		}
		
	};
	
	@Override
	public void performfp() {
		assert !alreadyIn:"this should never happen right? due to events `happening` sequentially";
		alreadyIn = true;
		try {
			if(!FactionsPlus.permission.has(sender, "factionsplus.moneytop")) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return;
			}
			if ( !Config._economy.isHooked() ) {
				sender.sendMessage( "Economy is unavailable or disabled in FactionsPlus configs" );
				return;
			}else {
				if (!Econ.isEnabled(usender)) {
					//XXX: even though we can still show the top without it!! at this point
					sender.sendMessage( "Economy is disabled in Factions plugin" );
					return;
				}
			}
			
			//get the page number requested
			String pageStr = this.arg( 0 );
			int page = 1;
			try {
				page = Integer.parseInt( pageStr );
			} catch ( Throwable t ) {
				// ignoring invalid page numbers
			}
			
			
			synchronized ( sortQueryLock ) {
				backgroundSortIfNeeded();
			
				showIfSorted_ElseQueueSender(page);
			}
			
			
			
		} finally {
			alreadyIn = false;
		}
	}
	
	private static Object sortQueryLock=new Object();
	private static final HashMap<CommandSender, Integer> notifyList=new HashMap<CommandSender, Integer>();

	private static final String	prefixing	= "  ";//for each entry in the top

	@SuppressWarnings( "boxing" )
	private void showIfSorted_ElseQueueSender( int page ) {
		
		synchronized ( sortQueryLock ) {
			if ( sortingTaskId<0 ) {// done sorting?
//				System.out.println("showing not queuing");
				showPage(sender, page);
			} else {
				// still sorting or about to begin, then queue the user->page to be shown at end of sorting
				// done: remember users that called this command and show results when done sorting, a hashmap of sender->page
				// must be careful to sync these
				notifyList.put(sender, page);
//				System.out.println("queued "+sender+" page="+page);
			}
		}//sync
	}
	@SuppressWarnings( "boxing" )
	public static final void showPage(CommandSender sender, int thePage) {
		int page = thePage;
		assert null != scoreBoard;
		assert null != sender;
		assert page>=1:page;
		//kinda done: make it look prettier + colors
		int max=scoreBoard.length;
		int pages=(max / shownPerPage)+(max % shownPerPage>0 ?1:0);
		if (page>pages) {
			page=pages;
		}
		sender.sendMessage( "Page "+page +" of "+pages);
		int startAt=(page-1)*shownPerPage;
		int endAt=Math.min(startAt+shownPerPage, max);
		int maxSeenTagLength=0;
		int maxSeenIdLength=0;
		int maxSeenMoneyLength=0;
		int maxSeenPositionLen=0;
		
		//find align data
		for ( int i = startAt; i < endAt; i++ ) {
			DualPack<Faction, Double> d = (DualPack<Faction, Double>)scoreBoard[i];
			Faction f = d.getFirst();
			maxSeenTagLength=Math.max(maxSeenTagLength, ChatColor.stripColor( f.getName() ).length());
			maxSeenIdLength=Math.max(maxSeenIdLength, f.getId().length());
			maxSeenMoneyLength=Math.max(maxSeenMoneyLength, Config._economy.getFormatted( d.getSecond().doubleValue()).length() );
			maxSeenPositionLen=Math.max(maxSeenPositionLen, Integer.toString( i+1).length() );
		}
		
		//actually show
		for ( int i = startAt; i < endAt; i++ ) {
			DualPack<Faction, Double> d = (DualPack<Faction, Double>)scoreBoard[i];
			Faction f = d.getFirst();
			//done: align to max (seen?) tag length
			sender.sendMessage(
				String.format("%s%s%"+maxSeenPositionLen+"d. %s%"+maxSeenTagLength+"s%s [%"+maxSeenIdLength+"s] %s: %s%"+
			maxSeenMoneyLength+"s", 
					prefixing, ChatColor.AQUA, i+1, ChatColor.RESET, f.getName(), ChatColor.DARK_BLUE, f.getId(),
					ChatColor.RESET, ChatColor.YELLOW, Config._economy.getFormatted( d.getSecond().doubleValue()))
			);
		}
	}


	private void backgroundSortIfNeeded() {
		
		synchronized ( sortQueryLock ) {
			if ( sortingTaskId >= 0 ) {// was scheduled, possibly still running or not yet started
				BukkitScheduler sched = Bukkit.getServer().getScheduler();
				if ( sched.isCurrentlyRunning( sortingTaskId ) ) {
					sender.sendMessage( "Please wait still sorting " + sortingCode.getStatus() + " factions... " );
					return;
				} else {
					sender.sendMessage( "Please wait the sorting is scheduled to start soon..." );
					return;
				}
			} else {// else was not yet scheduled or it ran and stopped, try rerun
				long timeSinceLastSort = System.currentTimeMillis() - lastSortTimeStamp;
				if ( ( timeSinceLastSort >= MILISEC_BETWEEN_SORTS ) || ( null == scoreBoard ) ) {
					// else not running & was stopped, start again
					//queue only if needs to sort
					sortingCode.setParam( sender );
					sortingTaskId =
						Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask( FactionsPlus.instance, sortingCode );
				}
			}
		}
	}


	private final static void mayClean() {
		shouldClean = true;
		if ( cleaningTaskId < 0 ) {
			cleaningTaskId =
				Bukkit.getServer().getScheduler()
					.scheduleAsyncDelayedTask( FactionsPlus.instance, cleanTask, TICKS_BEFORE_CLEAN );
		}else{
			//already scheduled but did not yet run ? fine with me
		}
	}
	
	
	private final static void mayNOTClean() {
		shouldClean = false;
		if ( cleaningTaskId >= 0 ) {
			BukkitScheduler sched = Bukkit.getServer().getScheduler();
			
			//FIXME: prevent infinite loop by adding a timeout, tho this will never infini loop if bukkit works ok
			//wait for the simple cleaning thread to finish before canceling it
			while (sched.isCurrentlyRunning( cleaningTaskId )) {
				Thread.yield();
			}
			
			sched.cancelTask( cleaningTaskId );
			
			//wait for the simply cleaning thread to finish just in case it started meanwhile
			while (sched.isCurrentlyRunning( cleaningTaskId )) {
				Thread.yield();
			}
			
			cleaningTaskId=-1;
		}//else already done or not scheduled
	}
	
	
	
}
