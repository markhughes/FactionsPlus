package markehme.factionsplus.sublisteners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusScoreboard;
import markehme.factionsplus.FactionsPlusUpdate;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FType;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsRelationChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CoreSubListener {
	
	public EventFactionsRelationChange eventFactionsRelationChange(EventFactionsRelationChange event) {
		FPUConf fpuconf = FPUConf.get(event.getUSender());
		
		if(FType.valueOf(event.getFaction()) == FType.WILDERNESS || FType.valueOf(event.getOtherFaction()) == FType.WILDERNESS) {
			if(fpuconf.fixes.get("disallowChangingRelationshipToWilderness")) {
				event.getUSender().msg(Txt.parse(LConf.get().fpNoAlterRelationship));
				event.setCancelled(true);
				return event;
			}
		}
		
		if(FType.valueOf(event.getFaction()) == FType.SAFEZONE || FType.valueOf(event.getOtherFaction()) == FType.SAFEZONE) {
			if(fpuconf.fixes.get("disallowChangingRelationshipToSafezone")) {
				event.getUSender().msg(Txt.parse(LConf.get().fpNoAlterRelationship));
				event.setCancelled(true);
				return event;
			}
		}
		
		if(FType.valueOf(event.getFaction()) == FType.WARZONE || FType.valueOf(event.getOtherFaction()) == FType.WARZONE) {
			if(fpuconf.fixes.get("disallowChangingRelationshipToWarzone")) {
				event.getUSender().msg(Txt.parse(LConf.get().fpNoAlterRelationship));
				event.setCancelled(true);
				return event;
			}
		}
		return event;
	}
	
	public ProjectileLaunchEvent projectileLaunchEvent(ProjectileLaunchEvent event) {
		if(!(event.getEntity() instanceof Player)) return event;
		
		// Ensure we're using a splash potion 
		if(event.getEntityType() == EntityType.SPLASH_POTION && ((Player)event.getEntity().getShooter()).isFlying()) {
			
			// So it is a splash potion, the player is flying, and the config disallows it
			((Player) event.getEntity().getShooter()).sendMessage(Txt.parse(LConf.get().flyCantUseSplashPotion));
			event.setCancelled(true);
		}
		
		return event;
	}
	
	public EntityShootBowEvent entityShootBowEvent(EntityShootBowEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			// Is the player flying, not an op, and not using admin mode?
			if(player.isFlying() && !player.isOp() && !UPlayer.get((Player) event.getEntity()).isUsingAdminMode()) {
				player.sendMessage(Txt.parse(LConf.get().flyCantAttack));
				event.setCancelled(true);
			}
		}
		
		return event;
	}
	
	public EntityDamageByEntityEvent entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			
			// Is the player flying, not an op, and not using admin mode?
			if(damager.isFlying() && !damager.isOp() && !UPlayer.get(damager).isUsingAdminMode()) {
				damager.sendMessage(Txt.parse(LConf.get().flyCantAttack));
				event.setCancelled(true);
			}	
		}
		
		return event;
	}
	
	public PlayerMoveEvent playerMoveEvent(PlayerMoveEvent event) {
		// Check for permission factionsplus.flightinterritory
		if(!event.getPlayer().isOp() && !UPlayer.get(event.getPlayer()).isUsingAdminMode()) {
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
		
		return event;
	}
	
	public EventFactionsChunkChange eventFactionsChunkChange(EventFactionsChunkChange event) {
		if(!event.getUSender().getFaction().isNone() && !event.getUSender().isUsingAdminMode() && !event.getUSender().getPlayer().isOp() && !FactionsPlus.permission.has(event.getUSender().getPlayer(), "factionsplus.bypassregioncheck")) {
			if(Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
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
		return event;
	}
	
	public EntityDamageEvent entityDamageEvent(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) return event;
		
		UPlayer uPlayer = UPlayer.get((Player) event.getEntity());

		//FPUConf fpuconf = FPUConf.get(uPlayer);
		
		Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()));
		
		if(FType.valueOf(factionAt) == FType.SAFEZONE) event.setCancelled(true);

		return event;
	}
	
	public InventoryClickEvent inventoryClickEvent(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			UPlayer uPlayer = UPlayer.get(event.getWhoClicked());
	
			if(event.getInventory().getType() == InventoryType.MERCHANT && !uPlayer.isUsingAdminMode()) {
				
				Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()));
				
				if(factionAt != uPlayer.getFaction() && !factionAt.isNone() && FType.valueOf(factionAt) != FType.SAFEZONE) {
					event.setCancelled(true);
					uPlayer.msg(Txt.parse(LConf.get().protectionCantUseVillager));	
				}
			}
		}
		return event;
	}
	
	public EventFactionsCreate eventFactionsCreate(EventFactionsCreate event) {
		String upperFactionName = Character.toUpperCase(event.getFactionName().charAt(0)) + event.getFactionName().substring(1);
		
		Faction faction = Faction.get(event.getFactionId());
		faction.setName(upperFactionName);
		
		return event;
	}
	
	public PlayerFishEvent playerFishEvent(PlayerFishEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		Faction factionAtPlayer = BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()));
		
		// Are they in their own Faction land, or in wilderness
		if(factionAtPlayer != uPlayer.getFaction() && !factionAtPlayer.isNone() && FType.valueOf(factionAtPlayer) != FType.SAFEZONE) {
			
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().protectionCantFishHere));
			
			return event; // No further checks required 
		}
        
		// Check location of hook (hook could be in, player could be out) 
		
		Faction factionAtHook = BoardColls.get().getFactionAt(PS.valueOf(event.getHook().getLocation())); 
		
		if(factionAtHook != uPlayer.getFaction() && !factionAtHook.isNone() && FType.valueOf(factionAtHook) != FType.SAFEZONE) {
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().protectionCantFishHere));
			
			return event;
		}
		
		return event;
	}
	
	public PlayerInteractEntityEvent playerInteractEntityEvent(PlayerInteractEntityEvent event) {
	    Entity currentEntity = event.getRightClicked();
		FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
	    UPlayer uPlayer = UPlayer.get(event.getPlayer());
	    
	    if(fpuconf.allowFactionInteract.containsKey(event.getRightClicked().getType())){
	    	if(!fpuconf.allowFactionInteract.get(event.getRightClicked().getType())) {
	    		Faction entityAt = BoardColls.get().getFactionAt( PS.valueOf( currentEntity.getLocation() ));
	    		
	    		if(entityAt.getId() != uPlayer.getFactionId() && FType.valueOf(entityAt) == FType.FACTION) {
	    			uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
	    			uPlayer.getPlayer().updateInventory();
		    	}
		    }	    	
	    }
	    
	    Player player = event.getPlayer();
	    
	    
	    
		if(((event.getRightClicked() instanceof Cow && player.getItemInHand().getType().equals(Material.BUCKET) || (event.getRightClicked() instanceof MushroomCow && player.getItemInHand().getType().equals(Material.BOWL))))
				&& FPUConf.get(UPlayer.get(event.getPlayer())).strictFarming
		) {
			
			Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
			
			if(factionAt != uPlayer.getFaction() && !factionAt.isNone() && FType.valueOf(factionAt) != FType.SAFEZONE) {
				
				event.setCancelled(true);
				uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
				
				return event; // No further checks required 
			}
			
			// Also check location of cow (player could be outside area, but cow inside / using hacks)
			
			Faction factionAtCow = BoardColls.get().getFactionAt(PS.valueOf( event.getRightClicked().getLocation()));
				
			if(factionAtCow != uPlayer.getFaction() && !factionAtCow.isNone() &&  FType.valueOf(factionAtCow) != FType.SAFEZONE) {
					
				event.setCancelled( true );
				uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
				
				return event;	
			}
		}
		
		return event;
	}
	
	public PlayerShearEntityEvent playerShearEntityEvent(PlayerShearEntityEvent event) {
	    Player player = event.getPlayer();
	    UPlayer uPlayer = UPlayer.get(player);

	    if(event.getEntity() instanceof Sheep) {
	        
	        Faction factionAt = BoardColls.get().getFactionAt(PS.valueOf( player.getLocation()));
	        
	        // Are they in their own Faction land, or in wilderness
	        if(factionAt != uPlayer.getFaction() && !factionAt.isNone()) {
	        	event.setCancelled(true);
	        	uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
	        	
	        	return event; // No further checks required
	        }
	        
	        // Check location of sheep (sheep could be inside, player could be outside / using hacks)
			
			Faction factionAtSheep = BoardColls.get().getFactionAt(PS.valueOf(event.getEntity().getLocation()));
			
			if(factionAtSheep != uPlayer.getFaction() && !factionAtSheep.isNone() &&  FType.valueOf(factionAtSheep) != FType.SAFEZONE) {
				event.setCancelled(true);
				uPlayer.msg(Txt.parse(LConf.get().protectionCantInteract));
				return event; 
			}
	    }
	    
		return event;
	}
	
	
	public PlayerJoinEvent playerJoinEvent(PlayerJoinEvent event) {
		
		UPlayer uPlayer = UPlayer.get(event.getPlayer());

		// Feature: Update Checks 
		if((uPlayer.isUsingAdminMode() || event.getPlayer().isOp()) && FactionsPlusUpdate.isUpdateAvailable()) {
			event.getPlayer().sendMessage(ChatColor.GOLD + "[FactionsPlus] " + ChatColor.WHITE + "Attention: There is an update available for FactionsPlus! Please upgrade ASAP." );
		}
				

		// Feature: scoreboards
		if(FPUConf.get(UPlayer.get(event.getPlayer())).scoreboardTopFactions) {
			if(FactionsPlusScoreboard.scoreBoard != null) {
				if(!( FactionsPlus.permission.has(event.getPlayer(), "factionsplus.hidesb." + event.getPlayer().getWorld().getName() ) && ! FactionsPlus.permission.has( event.getPlayer(), "factionsplus.hidesb" ) ) || FactionsPlus.permission.has(event.getPlayer(), "factionsplus.forcesb") ) {
					event.getPlayer().setScoreboard(FactionsPlusScoreboard.scoreBoard);
				}
			}	
		}
			
		// Feature: factionJoinLeaveMessagesLockedToFaction
		if(FPUConf.get(uPlayer.getUniverse()).factionJoinLeaveMessagesLockedToFaction) {
			event.setJoinMessage(null);
			for(Player onlineMember : uPlayer.getFaction().getOnlinePlayers()) {
				onlineMember.sendMessage(Txt.parse(LConf.get().fpFactionPlayerJoin, event.getPlayer().getDisplayName()));
			}
		}
		
		return event;
	}
	
	public PlayerQuitEvent playerQuitEvent(PlayerQuitEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
        
		if(FPUConf.get(uPlayer.getUniverse()).factionJoinLeaveMessagesLockedToFaction) {
			event.setQuitMessage(null);
			
			for(Player onlineMember : uPlayer.getFaction().getOnlinePlayers()) {
				onlineMember.sendMessage(Txt.parse(LConf.get().fpFactionPlayerLeave, event.getPlayer().getDisplayName()));
			}
		}
		
		return event;
	}
	
	public PlayerCommandPreprocessEvent playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
				
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
				return event;
			}
		}
		
		// TODO: Block Command on radius feature (see #57)
		
		return event;
	}
	
	public EventFactionsMembershipChange eventFactionsMembershipChange(EventFactionsMembershipChange event) {
		FPUConf fpuconf = FPUConf.get(event.getUSender().getUniverse());
		
		if(event.getReason() == MembershipChangeReason.JOIN ) {
			FactionData fData = FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getNewFaction().getId());

			if(fData == null) {
				FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).create(event.getUPlayer().getFactionId());
				fData = FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getNewFaction().getId());
			}
			
			// Show rules on join if configured
			if(FPUConf.get(event.getUPlayer().getUniverse()).showRulesOnJoin) {
				
				
				
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
			
			return event;
		}
		
		// Remove data on leave if they're the last player 
		if(event.getReason() == MembershipChangeReason.LEAVE) { 
			Faction faction = event.getUPlayer().getFaction();
			if (faction.getUPlayers().size() == 1) {
				
				if(fpuconf.peacefulCantDisband) {
					event.getUPlayer().sendMessage(Txt.parse(LConf.get().fpPeacefulCantDisband));
					event.setCancelled(true);
					return event;
				}
				
				// Last player, so remove all data
				if(FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender()) != null) {
					FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender().detach());
				}
			}
			
			return event;
		}
		
		// Remove data on disband 
		if(event.getReason() == MembershipChangeReason.DISBAND) {
			if(fpuconf.peacefulCantDisband) {
				// We need to find and notify the leader..
				for(Player playerMember : event.getUPlayer().getFaction().getOnlinePlayers()) {
					UPlayer testPlayer = UPlayer.get(playerMember);
					if(testPlayer.getRole() == Rel.LEADER) {
						event.getUPlayer().sendMessage(Txt.parse(LConf.get().fpPeacefulCantDisband));
					}
				}
				
				// Cancel the event
				event.setCancelled(true);
				return event;
			}
			
			// Only want to try and detach once - all members get disband event (see CmdFactionsDisband in Factions)
			if(event.getUSender().getRole() == Rel.LEADER) {
				if(FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender()) != null) {
					FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getUSender().detach());
				}
			}
			
			return event;
		}
		
		return event;
	}
	
	public PlayerDeathEvent playerDeathEvent(PlayerDeathEvent event) {

		final Player currentPlayer = (Player) event.getEntity();
		
		FType fType = FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(currentPlayer.getLocation())));
		
		if(!FactionsPlus.permission.has(currentPlayer, "factionsplus.keepItemsOnDeath."+fType.getNiceName())) return event;
		
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
				currentPlayer.updateInventory();

			}
		});
		
		return event;
	}
	
	public InventoryOpenEvent inventoryOpenEvent(InventoryOpenEvent event) {
		// Because of the intentions of this option, we're also going to block any inventory holder
		// aka furnace, hopper, dispencer - so we won't do any extra checks. 
		
		Boolean isPeacefulLocation = BoardColls.get().getFactionAt(PS.valueOf(event.getPlayer().getLocation())).getFlag(FFlag.PEACEFUL);
		
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		if(!uPlayer.getFaction().getFlag(FFlag.PEACEFUL) && isPeacefulLocation) {
			uPlayer.msg(Txt.parse(LConf.get().fpPeacefulChestProtected));
			event.setCancelled(true);
		}
		
		return event;
	}
	
	public EventFactionsDisband eventFactionsDisband(EventFactionsDisband event) {
		FPUConf fpuconf = FPUConf.get(event.getUSender().getUniverse());
		
		if(fpuconf.peacefulCantDisband) {
			event.getUSender().sendMessage(Txt.parse(LConf.get().fpPeacefulCantDisband));
			event.setCancelled(true);
		}
		
		return event;

	}
}
