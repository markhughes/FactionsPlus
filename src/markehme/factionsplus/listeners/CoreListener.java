package markehme.factionsplus.listeners;

import java.util.ArrayList;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusScoreboard;
import markehme.factionsplus.FactionsPlusUpdate;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.factions.event.EventFactionsRelationChange;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;


public class CoreListener implements Listener {
	
	public static Server fp;
	
	/**
	 * Fixes issues with changing relationship between wilderness/safezone/warzone if
	 * it is enabled in the configuration. 
	 * 
	 * @param event
	 */
	@EventHandler
	public void onFactionsEventRelationChange(EventFactionsRelationChange event) {
		
		FPUConf fpuconf = FPUConf.get(event.getUSender());
		
		if(!fpuconf.enabled) return; // Universe support
		
		if(FType.valueOf(event.getFaction()) == FType.WILDERNESS || FType.valueOf(event.getOtherFaction()) == FType.WILDERNESS) {
			if(fpuconf.fixes.get("disallowChangingRelationshipToWilderness")) {
				event.getUSender().msg(Txt.parse(LConf.get().fpNoAlterRelationship));
				event.setCancelled(true);
				return;
			}
		}
		
		if(FType.valueOf(event.getFaction()) == FType.SAFEZONE || FType.valueOf(event.getOtherFaction()) == FType.SAFEZONE) {
			if(fpuconf.fixes.get("disallowChangingRelationshipToSafezone")) {
				event.getUSender().msg(Txt.parse(LConf.get().fpNoAlterRelationship));
				event.setCancelled(true);
				return;
			}
		}
		
		if(FType.valueOf(event.getFaction()) == FType.WARZONE || FType.valueOf(event.getOtherFaction()) == FType.WARZONE) {
			if(fpuconf.fixes.get("disallowChangingRelationshipToWarzone")) {
				event.getUSender().msg(Txt.parse(LConf.get().fpNoAlterRelationship));
				event.setCancelled(true);
				return;
			}
		}
	}
	
	/**
	 * Configuration option required: flyCantUseSplashPotion
	 * Prevents a player from using splash potions while flying if enable in the configuration. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerThrowPotion(ProjectileLaunchEvent event) {
				
		// Ensure a player threw it 
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if(!FPUConf.get(UPlayer.get(event.getEntity()).getUniverse()).enabled) return; // Universe support

		
		// Ensure we're using a splash potion 
		if ((event.getEntityType() == EntityType.SPLASH_POTION) && (((Player)event.getEntity().getShooter()).isFlying()) && !FPUConf.get(UPlayer.get((Player) event.getEntity())).allowSplashPotionsWhileFlying) {
			
			// So it is a splash potion, the player is flying, and the config disallows it
			((Player)event.getEntity().getShooter()).sendMessage(Txt.parse(LConf.get().flyCantUseSplashPotion));
			event.setCancelled(true);
		}
	}
	
	/**
	 * Configuration option required: allowAttackingWhileFlying
	 * Prevents a player from using attacking while flying if enable in the configuration. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBowShoot(EntityShootBowEvent event) {
		
		// Ensure a player threw it 
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if(!FPUConf.get(UPlayer.get(event.getEntity()).getUniverse()).enabled) return; // Universe support
		
		// Flying Player can't damage others
		if(!FPUConf.get(UPlayer.get((Player) event.getEntity())).allowAttackingWhileFlying) {
			
			Player player = (Player) event.getEntity();
			
			// Is the player flying, not an op, and not using admin mode?
			if(player.isFlying() && !player.isOp() && !UPlayer.get((Player) event.getEntity()).isUsingAdminMode()) {
				player.sendMessage(Txt.parse(LConf.get().flyCantAttack));
				event.setCancelled(true);
			}
		}
	}
	
	/**
	 * Configuration option required: allowAttackingWhileFlying
	 * Prevents a player from using attacking while flying if enable in the configuration. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerAttack(final EntityDamageByEntityEvent event) {
		
		Entity e = event.getEntity();
		
		// Ensure configuration option is enabled, and it is PvP
		if(e instanceof Player && event.getDamager() instanceof Player) {
	
			if(!FPUConf.get(UPlayer.get((Player) e).getUniverse()).enabled) return; // Universe support

			if(!FPUConf.get(UPlayer.get((Player) event.getEntity())).allowAttackingWhileFlying) {
				Player damager = (Player) event.getDamager();
				
				// Is the player flying, not an op, and not using admin mode?
				if(damager.isFlying() && !damager.isOp() && !UPlayer.get(damager).isUsingAdminMode()) {
					damager.sendMessage(Txt.parse(LConf.get().flyCantAttack));
					event.setCancelled(true);
				}	
			}
		}
	}
	
	/**
	 * Permission required: factionsplus.flightinownterritory
	 * If a player has this permission, allow them to fly in their own territory. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return; // Universe support
		
		// Check for permission factionsplus.flightinterritory
		if(FactionsPlus.permission.has(event.getPlayer(), "factionsplus.flightinownterritory") && !event.getPlayer().isOp() && !UPlayer.get(event.getPlayer()).isUsingAdminMode()) {
			
			// Check where they are
			if(BoardColls.get().getFactionAt(PS.valueOf(event.getPlayer().getLocation())).getId() == UPlayer.get(event.getPlayer()).getFactionId()) {
				event.getPlayer().setAllowFlight(true);
			} else {
				// Are they flying? 
				if(event.getPlayer().isFlying()) {
					event.getPlayer().setAllowFlight(false);
				}
			}
		}
	}
	
	/**
	 * Configuration option required: enableWorldGuardRegionCheck
	 * Prevents players (unless they have factionsplus.bypassregioncheck or factionsplus.allowregionclaim.<id>) to claim
	 * inside of a WorldGuard region.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLandClaim(EventFactionsChunkChange event) {
		
		FPUConf fpuconf = FPUConf.get(event.getUSender());
		
		if(!fpuconf.enabled) return; // Universe support
		
		if(fpuconf.enableWorldGuardRegionCheck) {
			
			if(event.getUSender() == null) return;
			
			if(!event.getUSender().getFaction().isNone() && !event.getUSender().isUsingAdminMode() && !event.getUSender().getPlayer().isOp() && !FactionsPlus.permission.has(event.getUSender().getPlayer(), "factionsplus.bypassregioncheck")) {
				// Ensure WorldGuard exists! 
				if(Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
					
					if(FactionsPlus.worldGuardPlugin != null ) {
						
						// Check for chunks
						if(Utilities.checkForRegionsInChunk(event.getUSender().getPlayer().getLocation(), event.getUSender().getPlayer())) {
							
							// TODO: 	Check region flags - this can be done by instead returning an array
							//			of the regions in the chunk, checking the size of the array and then
							//			loop through if we're supporting checkForWorldGuardRegionFlags
							
							// The flag will be "faction" /region flag SomeRegion faction MyFactionName
							// and thus, that Faction can build. 
							
							event.setCancelled(true);
							
							event.getUSender().msg(Txt.parse(LConf.get().worldGuardRegionInWay));
								
						}
					}
				}
			}
		}
	}
	
	/**
	 * Configuration option required: makeSafeZoneExtraSafe
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		
		if(event.getEntity() instanceof Player) {
			
			UPlayer uPlayer = UPlayer.get((Player) event.getEntity());

			FPUConf fpuconf = FPUConf.get(uPlayer);
			
			if(!fpuconf.enabled) return; // Universe support
			if(!fpuconf.makeSafeZoneExtraSafe) return;
			
			Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()));
			
			if(FType.valueOf(factionAt) == FType.SAFEZONE) {
				event.setCancelled(true);
			}
		}
	}
	
	/**
	 * Configuration option required: disallowAccessToVillagersToOtherFactions
	 * Disallow interaction with villagers if enabled in config.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onVillagerTrade(InventoryClickEvent event) {
		
		if(event.getWhoClicked() instanceof Player) {
			
			UPlayer uPlayer = UPlayer.get(event.getWhoClicked());

			if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // Universe support
			if(!FPUConf.get(uPlayer.getUniverse()).disallowAccessToVillagersToOtherFactions) return;
			
			// detects villager trade with player
			if(event.getInventory().getType() == InventoryType.MERCHANT && !uPlayer.isUsingAdminMode()) {
				
				Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()));
				
				if(factionAt != uPlayer.getFaction() && !factionAt.isNone() && FType.valueOf(factionAt) != FType.SAFEZONE) {
					event.setCancelled(true);
					uPlayer.msg(Txt.parse(LConf.get().protectionCantUseVillager));	
				}
			}
		}
		
	}
	
	/**
	 * Configuration Option: factionNameForceFirstLetterUppercase
	 * If enabled, this will force the first letter of a Faction name to uppercase. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateFaction(EventFactionsCreate event) {
		if(!FPUConf.get(event.getUniverse()).enabled) return;
		
		if(FPUConf.get(event.getUniverse()).factionNameForceFirstLetterUppercase) {
			
			String upperFactionName = Character.toUpperCase(event.getFactionName().charAt(0)) + event.getFactionName().substring(1);
			
			Faction wFaction = Faction.get(event.getFactionId());
			
			wFaction.setName(upperFactionName);
			
		}
	}
	
	/**
	 * Configuration Option: strictFarming
	 * Part of stricter farming 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerFish(PlayerFishEvent event) {
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return;
		
		if(FPUConf.get(UPlayer.get(event.getPlayer())).strictFarming) {
			
			UPlayer uPlayer = UPlayer.get(event.getPlayer());
			
			Faction factionAtPlayer = BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()));
			
			// Are they in their own Faction land, or in wilderness
			if(factionAtPlayer != uPlayer.getFaction() && !factionAtPlayer.isNone() && FType.valueOf(factionAtPlayer) != FType.SAFEZONE) {
				
				event.setCancelled(true);
				uPlayer.msg(Txt.parse(LConf.get().protectionCantFishHere));
				
				return; // No further checks required 
			}
	        
			// Check location of hook (hook could be in, player could be out) 
			
			Faction factionAtHook = BoardColls.get().getFactionAt(PS.valueOf(event.getHook().getLocation())); 
			
			if(factionAtHook != uPlayer.getFaction() && !factionAtHook.isNone() && FType.valueOf(factionAtHook) != FType.SAFEZONE) {
				event.setCancelled(true);
				uPlayer.msg(Txt.parse(LConf.get().protectionCantFishHere));
				
				return;
			}
		}
	}
	
	/**
	 * Requires Configuration option: allowFactionInteract
	 * If the EntityType is in allowFactionInteract and set to false, then
	 * players can not interact with it in another Factions territory. 
	 * @param event
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
	    
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;
		
	    Entity currentEntity = event.getRightClicked();
	    // disable animal stealing of horses (with a leash) / pigs (also stops inventory stealing - I think)
	    
	    if(FPUConf.get(uPlayer).allowFactionInteract.containsKey(event.getRightClicked().getType())){
	    	if(!FPUConf.get(uPlayer).allowFactionInteract.get(event.getRightClicked().getType())) {
	    		Faction entityAt = BoardColls.get().getFactionAt( PS.valueOf( currentEntity.getLocation() ));
	    		
	    		if(entityAt.getId() != uPlayer.getFactionId() && FType.valueOf(entityAt) == FType.FACTION) {
	    			uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
	    			uPlayer.getPlayer().updateInventory();
		    	}
		    }	    	
	    }

	}
	
	/**
	 * Configuration Option: strictFarming
	 * If stricter farming is enabled, then disallow interacting with cows. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMilkEvent( PlayerInteractEntityEvent event ) {
		Player player = event.getPlayer();
		
		UPlayer uPlayer = UPlayer.get(player);
		
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;
		
		if(((event.getRightClicked() instanceof Cow && player.getItemInHand().getType().equals(Material.BUCKET) || (event.getRightClicked() instanceof MushroomCow && player.getItemInHand().getType().equals(Material.BOWL))))
			&& FPUConf.get(UPlayer.get(event.getPlayer())).strictFarming
		) {
			
			Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
			
			if(factionAt != uPlayer.getFaction() && !factionAt.isNone() && FType.valueOf(factionAt) != FType.SAFEZONE) {
				
				event.setCancelled(true);
				uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
				
				return; // No further checks required 
			}
			
			// Also check location of cow (player could be outside area, but cow inside / using hacks)
			
			Faction factionAtCow = BoardColls.get().getFactionAt(PS.valueOf( event.getRightClicked().getLocation()));
			
			if(factionAtCow != uPlayer.getFaction() && !factionAtCow.isNone() &&  FType.valueOf(factionAtCow) != FType.SAFEZONE) {
				
				event.setCancelled( true );
				uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
				
				return;
				
			}
		}
	}
    
	/**
	 * Configuration Option: strictFarming
	 * If stricter farmer is enabled, disallow sheering sheep outside
	 * of a players territory. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
	    Player player = event.getPlayer();
	    UPlayer uPlayer = UPlayer.get( player );
	        
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;
		
	    if(event.getEntity() instanceof Sheep && FPUConf.get(UPlayer.get(event.getPlayer())).strictFarming) {
	        
	        Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf( player.getLocation()));
	        
	        // Are they in their own Faction land, or in wilderness
	        if(factionAt != uPlayer.getFaction() && !factionAt.isNone()) {
	        	event.setCancelled(true);
	        	uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
	        	
	        	return; // No further checks required
	        }
	        
	        // Check location of sheep (sheep could be inside, player could be outside / using hacks)
			
			Faction factionAtSheep = BoardColls.get().getFactionAt(PS.valueOf(event.getEntity().getLocation()));
			
			if(factionAtSheep != uPlayer.getFaction() && !factionAtSheep.isNone() &&  FType.valueOf(factionAtSheep) != FType.SAFEZONE) {
				event.setCancelled(true);
				uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
			}
	    }
	}
	
	/**
	 * Player Join handler (does a few things)
	 * Sends updates on join for ops/admins
	 * Sends scoreboard if scoreboardMapOfFactions is enabled
	 * Sends faction player join message (if factionJoinLeaveMessagesLockedToFaction enabled)
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
        
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;
		
		// Feature: Update Checks 
		// Notify the op/admin that there is an update available 
		if((UPlayer.get(event.getPlayer()).isUsingAdminMode() || event.getPlayer().isOp())
				&& FactionsPlusUpdate.updateAvailable ) {
			
			event.getPlayer().sendMessage( ChatColor.GOLD + "[FactionsPlus] " + ChatColor.WHITE + "Attention op: There is an update available for FactionsPlus! Please upgrade ASAP." );
		
		}
		
		// --------------------------------------------------
		// START: FactionsPlus universe-based features 
		// --------------------------------------------------
		
		// Ensure FactionsPlus is enabled in this universe 
		if(!FPUConf.get(UPlayer.get(uPlayer.getUniverse())).enabled) return;
		
		// Feature: scoreboards
		
		if(FPUConf.get(UPlayer.get(event.getPlayer())).scoreboardMapOfFactions) {
			if(FactionsPlusScoreboard.scoreBoard != null) {
				if(!( FactionsPlus.permission.has(event.getPlayer(), "factionsplus.hidesb." + event.getPlayer().getWorld().getName() ) && ! FactionsPlus.permission.has( event.getPlayer(), "factionsplus.hidesb" ) ) || FactionsPlus.permission.has(event.getPlayer(), "factionsplus.forcesb") ) {
					event.getPlayer().setScoreboard(FactionsPlusScoreboard.scoreBoard);
				}
			}	
		}
		
		if(FPUConf.get(uPlayer.getUniverse()).factionJoinLeaveMessagesLockedToFaction) {
			event.setJoinMessage(null);
			
			for(Player onlineMember : uPlayer.getFaction().getOnlinePlayers()) {
				onlineMember.sendMessage(Txt.parse(LConf.get().fpFactionPlayerJoin, event.getPlayer().getDisplayName()));
			}
		}
	}
	
	/**
	 * This event so far only notifies when a player is signing off in their Faction
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		UPlayer uPlayer = UPlayer.get(e.getPlayer());
        
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;
		
		if(FPUConf.get(uPlayer.getUniverse()).factionJoinLeaveMessagesLockedToFaction) {
			e.setQuitMessage(null);
			
			for(Player onlineMember : uPlayer.getFaction().getOnlinePlayers()) {
				onlineMember.sendMessage(Txt.parse(LConf.get().fpFactionPlayerLeave, e.getPlayer().getDisplayName()));
			}
		}
	}
	
	/**
	 * Configuration Option: denyCommandsIn
	 * Deny commands in certain territories as set in configuration
	 * @param event
	 */
	@EventHandler
	public void onRunBlockedCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		
		if(!FPUConf.get(UPlayer.get(player).getUniverse()).enabled) return;
		
		// Fetch the faction
		Faction factionHere = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
		
		// Grab the FType object for this faction
		FType fType = FType.valueOf(factionHere);
		
		// Fetch the blocked commands
		ArrayList<String> blockedCommands = FPUConf.get(event.getPlayer()).denyCommandsIn.get(fType);
		
		// Ensure we should run a check
		if(blockedCommands.size() > 0 && !player.isOp() && !UPlayer.get(player).isUsingAdminMode()) {
			// Search for the command
			if(blockedCommands.contains(event.getMessage().toLowerCase().split(" ")[0].replace("/" , ""))) {
				// Cancel it, and send a message to let them know
				event.setCancelled(true);
				player.sendMessage(Txt.parse(LConf.get().fpCommandDenied, fType.getNiceName()));
			}
		}
	}
	
	/**
	 * Configuration options: showRulesOnJoin
	 * Sends rules to player on join, also detects leaving and disbanding to
	 * clean up FactionData.
	 * @param event
	 */
	@EventHandler(priority=EventPriority.LOWEST)
	public void onFactionsMembershipChange(EventFactionsMembershipChange event) {
		
		if(!FPUConf.get(event.getUPlayer().getUniverse()).enabled) return;
		
		// Show rules on join if configured
		if(event.getReason() == MembershipChangeReason.JOIN ) {
			if(FPUConf.get(event.getUPlayer().getUniverse()).showRulesOnJoin) {
				FactionData fData = FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getNewFaction().getId());
				
				// Rules exist, send through rules
				if(fData.rules.size() > 0) {
					event.getUPlayer().msg(Txt.parse(LConf.get().rulesListingStart));
					
					int i = 0;
					
					for(String rule : fData.rules) {
						i++;
						event.getUPlayer().msg(i+". " + rule);
					}
				}
			}
			
			return;
		}
		
		// Remove data on leave if they're the last player 
		if(event.getReason() == MembershipChangeReason.LEAVE) { 
			Faction faction = event.getUPlayer().getFaction();
			if (faction.getUPlayers().size() == 1) {
				// Last player, so remove all data
				if(FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender()) != null) {
					FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender().detach());
				}
			}
			
			return;
		}
		
		// Remove data on disband 
		if(event.getReason() == MembershipChangeReason.DISBAND) {
			// Only want to try and detach once - all members get disband event (see CmdFactionsDisband in Factions)
			if(event.getUSender().getRole() == Rel.LEADER) {
				if(FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender()) != null) {
					FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender().detach());
				}
			}
			
			return;
		}
		
	}
	
	/**
	 * Permissions required: factionsplus.keepItemsOnDeath.x and factionsplus.keepExperienceOnDeath.x
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		final Player currentPlayer = (Player) event.getEntity();

		if(!FPUConf.get(UPlayer.get(currentPlayer).getUniverse()).enabled) return;
		
		Boolean keepOnWarzoneDeath = false;
		
		// ---- start migration stuff 
		// TODO: Remove 0.6 Migration in 1.0.0 stable release
		if(FactionsPlus.permission.has(currentPlayer, "factionsplus.keepItemsOnDeathInWarZone")) {
			// It's pretty silly for us to migrate this permission. Vault needs methods for this.
			
			keepOnWarzoneDeath = true; // just let it go
			
			/*Boolean permMigrated = false;
			
			for(String g : FactionsPlus.permission.getPlayerGroups(currentPlayer)) {
				if(FactionsPlus.permission.groupHas(currentPlayer.getWorld(), g, "factionsplus.keepItemsOnDeathInWarZone")) {
					if(FactionsPlus.permission.groupRemove(currentPlayer.getWorld(), g, "factionsplus.keepItemsOnDeathInWarZone")) {
						FactionsPlus.permission.groupAdd(currentPlayer.getWorld(), g, "factionsplus.keepItemsOnDeath.warzone");
						permMigrated = true;
					}
				}
			}*/
			
			FactionsPlus.info(ChatColor.GOLD + "The permission factionsplus.keepItemsOnDeathInWarZone in your permissions plugin should be changed to factionsplus.keepItemsOnDeath.WarZone");
			FactionsPlus.info(ChatColor.RED + "Please Migrate: factionsplus.keepItemsOnDeathInWarZone -> factionsplus.keepItemsOnDeath.WarZone + factionsplus.keepExperienceOnDeath.WarZone");
			
			FactionsPlus.debug("The permission factionsplus.keepItemsOnDeathInWarZone in your permissions plugin should be changed to factionsplus.keepItemsOnDeath.WarZone");
			FactionsPlus.debug("Otherwise, this will cause issues in the future for player " + currentPlayer.getName() + " ("+currentPlayer.getUniqueId().toString()+")");
			for(String g : FactionsPlus.permission.getPlayerGroups(currentPlayer)) {
				if(FactionsPlus.permission.groupHas(currentPlayer.getWorld(), g, "factionsplus.keepItemsOnDeathInWarZone")) {
					FactionsPlus.debug("Group " + g + " has factionsplus.keepItemsOnDeathInWarZone - PLEASE MIGRATE!");
				}
			}
			
			
		}
		
		
		// ---- end migration stuff
		
		FType fType = FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(currentPlayer.getLocation())));

		
		// TODO: Remove 0.6 Migration in 1.0.0 stable release (remove keepOnWarzoneDeath check here)
		if(!FactionsPlus.permission.has(currentPlayer, "factionsplus.keepItemsOnDeath."+fType.getNiceName()) && !keepOnWarzoneDeath) return;
		
		UPlayer uPlayer = UPlayer.get(currentPlayer);
		
		uPlayer.msg(Txt.parse(LConf.get().fpKeepInvDie, fType.getNiceName()));

		final ItemStack[] playersArmor = currentPlayer.getInventory().getArmorContents();
		final ItemStack[] playersInventory = currentPlayer.getInventory().getContents();
		
		// To be compatible with plugins that might force players to drop items on death, we only
		// remove items that're in their inventory or their armor. This can be bypassed with 
		// factionsplus.keepItemsOnDeath.neverdrop.x
		
		if(FactionsPlus.permission.has(currentPlayer, "factionsplus.keepItemsOnDeath.neverdrop."+fType.getNiceName())) {
			// Don't drop anything
			event.getDrops().clear();
		} else {
			// Remove armor from item drops 
			for (ItemStack is : playersArmor) {
				event.getDrops().remove(is);
			}
			
			// Remove inventory drops 
			for (int i = 0; i < playersInventory.length; i++) {
				event.getDrops().remove(playersInventory[i]);
			}
		}
		
		// Keep Players Experience (if configured) 
		if(FactionsPlus.permission.has(currentPlayer, "factionsplus.keepExperienceOnDeath."+fType.getNiceName())) {
			event.setDroppedExp(0);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(FactionsPlus.instance, new Runnable() {
			@Override
			public void run() {
				// Update the armor and inventory to their previous armor and inventory 
				currentPlayer.getInventory().setArmorContents(playersArmor);
				currentPlayer.getInventory().setContents(playersInventory);

			}
		});
	}
}
