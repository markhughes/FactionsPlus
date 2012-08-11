# FactionsPlus version 0.4.8

* new config option jails.`canJailOnlyIfIssuerIsInOwnTerritory` when true (by default) the player issuing `/f jail` 
in an attempt to jail another player, must be inside its own faction territory. This should prevent 2+ players from jailing
each other to their advantage while they are near or inside enemy base.

* Fixed jail command: leaders can no longer jail themselves to escape from enemy territory

* Cobble monster griefing protection: changes behaviour of lava/water flow so that lava does not flowed between different boundaries (lava+water allowed the creation of cobblestone griefing, which could be done cross-border)

* fixed bad calls to metrics which would set `opt-out` option to `true` (only with 0.4.7) when server would shutdown or 
FactionsPlus plugin would be disabled `opt-out` is by default `false` which means that every plugin using metrics will send stats to mcstats.org  
  However now the option being `true` will cause all of those plugins to not send any stats (unless they modified metrics code
 to ignore the `opt-out` option and thus send stats anyway).  
  You can revert to metrics defaults by deleting the `plugins\PluginMetrics\config.yml` file OR by editing it and changing 
  `opt-out` to `false` but not before you make sure you're running FactionsPlus 0.4.8 (or just not the 0.4.7 version which 
  sets that to `true` as I said above). If you do no such thing, but you ran the 0.4.7 version at least once, then metrics stats
  will not be sent from your sever by any plugins that use metrics.

* Our plugin is now ignoring the `opt-out` option and will always send metrics stats.

* added config option `disableAutoCommentsInConfig` is true (false by default) it will omit the "### " (auto)comments that are 
prepended in front of the config options inside config.yml which explain what that config option does.

* new config option banning.`canBanToPreventFutureJoins` if set to true(as by default) it will allow preemptive banning of players
even if they are not already in your faction, so that they cannot join in the future, ie. if your faction is open and doesn't require invitations
or simply you just want to make sure nobody can invite a certain player to join in the future.

* config options `leadersCanFactionUnban` and `officersCanFactionUnban` have been removed  
the options `leadersCanFactionBan` and `officersCanFactionBan` have been upgraded to `leadersCanFactionBanAndUnban` and 
`officersCanFactionBanAndUnban`

* `/f ban` will no longer cause a player leave fee; will cause a FPlayerLeaveEvent with KICKED reason (which is cancellable)
previously it was LEAVE reason; the banned player is also deinvited from that faction;    
`factionsplus.ban` and `factionsplus.unban` permissions are completely ignored, please use `factionsplus.banunban` permission 
node instead which only has effect if the new option `furtherRestrictBanUnBanToThoseThatHavePermission` is `true`(false by default) in config(explained by comments inside the config after one run)

* while `disableUpdateCheck` is `false` the checking for new version of the plugin will be done automatically every time 
the plugin is enabled(ie. bukkit server start) and every 24 hours.

* you're now informed on console when the state changes for teleports listening due to ie. `/f reloadfp` and you changing the
options inside the `Teleports:` section. For example turning them all to false will stop listening for teleport events, while
turning any from false to true will start listening (unless it was already listening).

* extras.Protection.LWC.`removeAllLocksOnClaim` is automatically set to true if `onCaptureResetLwcLocks` in Factions plugin
is set to true. This doesn't save the setting in the config.yml, you'll have to do it yourself.

* You may use `/f debug configdiff` to see the config options that you've changed from the default (OPs/console only)

* every config option inside config.yml now has above it auto comments(lines starting with "### ") which explain what 
the config option does. These comments are automatically removed and updated on every config reload(which also does a save) 
to be sure they are always up to date with the currently running version of FactionsPlus.  
If you want to place and keep your own comments just use normal comments ( lines starting with one "#" ) they'll be kept 
inside the config.yml during saves. A save occurs on every config reload (ie. when plugin is enabled or `/f reloadfp`)  
You may also notice that we include the dotted format of the config option right above it ie. `jails.enabled` would be right above
`enabled`, however do remember that you cannot use dotted format in config names. These are there for copy/pasting simplicity
when referring to config options and an easier way to see which option you're really looking at without having to check for its
parent sections.

* in config.yml, all comments starting with "### " (3 # and a space) instead of just "#" are automatically discarded.

* jailed players are now returned to their original location, as long as they were online when they were unjailed 
(but also when they were jailed)  
if they were offline while unjailed, they retain the position they had upon logoff (same as in 0.4.7)  
if a player was jailed while offline it will only be teleported to the jail upon login (same as in 0.4.7)  
if all else fails, when unjailed the player will be teleported back to bed or world spawn  
`factionsplus.unjail` permission is completely ignored, but if the new option `furtherRestrictJailUnjailToThoseThatHavePermission` is `true` (false by default)
then `factionsplus.jailunjail` permission node is used to further restrict leaders/officers from using jail/unjail.

* fixed uses of getPlayer which were expected to act as getPlayerExact  

* new config option jails.`denyMovementWhileJailed` if `true`(by default) it will only allow the jailed player to look 
around, but not move from the place where the jail was set(constrained to 1 block), 
even if there are no blocks below feet. When `false` the 
jailed player can move around and can possibly be broken out of jail by other members if they're allowed to break 
blocks in that region (ie. if no /f owner or /f access on the chunk, or other stuff like /region ) or jail can be 
tnt cannon-ed etc.

* fixed /f removewarp  not removing warps 

* fixes for NPEs from issue 60

* fixes /f help NPE on last page

* for Factions 1.7 the TRUCE relationship is handled the same as NEUTRAL when considering denying or reporting teleports `Teleports.intoTerritory.*`

* added new config options to deny teleports via /back command that would end up inside  
ally/neutral/enemy territory Here they are with their defaults:  
  - `Teleports.intoTerritory.Ally.deny.ViaBackCommand`: false
  - `Teleports.intoTerritory.Enemy.deny.ViaBackCommand`: true
  - `Teleports.intoTerritory.Neutral.deny.ViaBackCommand`: false

* `/f money top [page=1]` shows the list of factions that have the most money ie. `/f money top 10` shows the 10th page
  - sorting happens at most once every 30 seconds and only when command is issued; if already sorted it uses the previously 
  sorted table
  - the sorted table is dereferenced from memory after 120 sec from the last use to allow gc freeing up some RAM
  - sorting happens in a worker thread in the background, results are shown to all requesting users when sorting is
  complete
  
* allow `/f debug` from console(or ingame OPs) and now shows workers

* added new config options to deny/report teleports via /home command or ender pearls that would land inside 
ally/neutral/enemy territory or into safezone/warzone. Here they are with their defaults:  
  - `Teleports.intoTerritory.Ally.deny.ViaHomeCommand`: false
  - `Teleports.intoTerritory.Ally.deny.ViaEnderPeals`: false
  - `Teleports.intoTerritory.Ally.reportOnConsole.ifTeleportCauseIs_Command`: false
  - `Teleports.intoTerritory.Ally.reportOnConsole.ViaEnderPeals`: false
  - `Teleports.intoTerritory.Enemy.deny.ViaHomeCommand`: true
  - `Teleports.intoTerritory.Enemy.deny.ViaEnderPeals`: true
  - `Teleports.intoTerritory.Enemy.reportOnConsole.ifTeleportCauseIs_Command`: true
  - `Teleports.intoTerritory.Enemy.reportOnConsole.ViaEnderPeals`: true
  - `Teleports.intoTerritory.Neutral.deny.ViaHomeCommand`: false
  - `Teleports.intoTerritory.Neutral.deny.ViaEnderPeals`: false
  - `Teleports.intoTerritory.Neutral.reportOnConsole.ifTeleportCauseIs_Command`: false
  - `Teleports.intoTerritory.Neutral.reportOnConsole.ViaEnderPeals`: false
  - `Teleports.intoTerritory.SafeZone.denyIfViaEnderPeals`: false
  - `Teleports.intoTerritory.WarZone.denyIfViaEnderPeals`: false  
  _
  -
Note that these are automatically added into your config, and the old config options that apply will be automatically
upgraded to these new ones, you don't have to add them manually but if you do, you'll have
realize that each "." actually represents a section ie. Teleports: then next line 2 spaces then intoTerritory: and so on 4 spaces...  
    Reporting is done on console only.  
    Denying will be instant, regardless of any warm-up delays other plugins may have.  
    It makes sure that you cannot exploit this by having home set outside enemy land and obstructing it to get you inside.  
    The expected console message upon report would look similar to this:    
    > 19:12:52 [INFO] [FactionsPlus] Player 's2' teleported into enemy land faction 'fac. Their last typed command: '/home my1'.  
    You may test this by making yourself op and using /home to tp into enemy territory. Which is denied by default, but
    OPs are exempt for any such denials but the reporting on console still happens for them.    
    The reported last command typed by the player that teleported is not necessarily(and usually unlikely to be) the cause of 
    the teleport, for example another player might've used /tphere. Or warm-up delays for teleports due to Essentials allowed the
    teleporting player to type more commands before the teleport event happened. For reasons like this and the fact that there's no
    real way of associating the command with the teleport event, we cannot know for sure which command(and of which player)
    actually caused the teleport to happen.  
    The exploitable /home prevetion in 0.4.7 is now fixed in 0.4.8 such that it's highly unlikely that it can be exploited
    mainly because we're now hooking into essentials (the plugin that has /home)  
    The used pearl is wasted and a message will show.

# FactionsPlus version 0.4.7

* max warps fixed

* auto update checks properly now. It's also in a new thread

* fixed situations in which, while using Factions 1.7, checking if a faction was or not SafeZone would yield true if
it had explosions disabled

* properly closing any resources which were previously leaking

* new command: `/f reloadfp [all|config|templates]` causes reloading of the specified config(s)
just in case you manually edited them, you now thus don't have to issue a bukkit 'reload' or start/stop server
permission used is the same as the one for Factions, namely `factions.reload` (for both v 1.7 and 1.6)  
Note that if you delete `config.yml` before issuing `/f reloadfp` then the created one will have the same settings as
the previous one except the comments (because those are not yet implemented: that is each option having it's own comment)

* FactionsPlus commands are shown in /f help in both Factions 1.6 and 1.7

* added new config option Teleports.`disallowTeleportingToEnemyLandViaEnderPeals`
if set(to true) this will prevent ender pearl teleports that land in enemy territory
(you can still teleport outside of enemy land as long as ender pearl does land outside it)
This is currently easily exploitable.

* new config options: Teleports.`disallowTeleportingToWarZoneViaEnderPeals` 
and Teleports.`disallowTeleportingToSafeZoneViaEnderPeals`
they prevent teleporting via ender pearls if they land into SafeZone/WarZone
the used pearl is wasted and a message will show.

* New config option Teleports.`disallowTeleportingToEnemyLandViaHomeCommand` which will deny all teleports caused by
"/home" command only if they would've landed you in an enemy faction (enemy is a relation between you and that faction)  

* new config option: powerboost.'respectFactionsWarZonePowerLossRules': Applies to both the wildernessPowerLoss and warZonePowerLoss settings and integrates a check for these in the powerboost listener

* new config option: powerboost.'extraPowerWhenKillMonster': self-explanatory
 
* fixed /f gc and /f cf commands to not error on console
  they won't work for Factions 1.7 due to it counting on other plugins like HeroChat to provide faction/global chat
  (this might be fixed in the future so that you can use these commands in 1.7 too)
  
* minimized overhead & memory-consumption when accessing the user defined config options at runtime

* LWC Locks Reset Fixing extras.Protection.LWC.`removeAllLocksOnClaim`
this will remove the locks in the claimed chunk, unless the locks are owned by anyone in the same faction you are in
in other words, locks owned by people in your faction(including you) won't be removed

* New command: /f clearlocks: faction admin accessible only, basically performs a clean of all non-faction owned protections in that chunk. Permission 'factionsplus.clearlwclocks'

* /f unban

* Bridge between 1.6 and 1.7, you only need one FactionsPlus.jar regardless of what Factions.jar you're using

* warpTeleportAllowedEnemyDistance

* blockCPublicAccessOnNonOwnFactionTerritory - will allow you to make a door truly public

* General bugfixes, including a bunch of NPEs, and other annoyances 

* - fixed /f togglestate to only charge you if we're hooked into economy  
  and to not cause internal error if inexistent faction tag was specified. 
  - If you have `factionsplus.togglestate.others` permission (or you are Op) you can toggle other factions
   only if you are (Op or)admin/officer/member in your faction if those were set in the config.
  - Prevent changing the state of WarZone, SafeZone or Wilderness, unless you are Op.
  - Even as Op, you are still charged the money (it's not like you can't afford it ;) ).
  - /f toggle yourfactiontag  is equivalent with /f toggle
  - Now tells you the faction tag which was affected by the change

* fixed NPE when joined while being previously in faction jail

* fixed spazzing out while in faction jail and tried to move once, you may now look around

* disallowed teleporting out while in faction jail either via commands like /home or via ender peals etc.

### config.yml features

* New Configuration setup

* obsolete config options are automatically upgraded/transformed into the newly named ones.  
  This implies that the old value is kept. You are also notified on console about this.

* invalid config options are automatically commented out.  You are also notified on console about this. 

* using obsolete/old config options combined with the new ones will auto comment the old ones with "# OVERRIDDEN..."
  so that you know that they are ignored. You are also notified on console about this.

* all subsequent (detected as)duplicate config options are automatically ignored and transformed into comments with
  a warning showing on console, the commented options are preceded by "# DUPLICATE #"
  You are also notified on console about this.
  
* comments `#` are kept on each save, you may add more or delete them.  
Comments are any lines whose first non-whitespace character is `#`.

### Known issues:
* `/f reloadfp` refreshes only the economy state, ie. if you disabled some options which were previously enabled they 
may still be in effect after reload (except economy that will auto enabled/disable on reload)

# FactionsPlus version 0.4.6

- fixed bug with extraPowerLossIfDeathBySuicide

- fixed bug with unjailing (I think?)

- fixed mysterious NoSuchFieldError related to /f help

- fixed showLastAnnounceOnLandEnter (WOO-HOOO!)

- fixed bug with /f fc

- added Metrics

- removed useless code

- general fixes/stability between the two versions

- fixes security bug related to banning players in other Factions

- fixes security bug related to jailing players in other Factions

# FactionsPlus version 0.4.5

- built against new Factions version

- added /f debug (only ops can run)

- added message to /f unjail

- added permission check to /f unjail

- moved to new leader/admin, officer/mod checking system - easier for me to program with

- help page fix for newest version (it's only now became more complex for me to handle the help pages)

- added updating checking system (I really hope this doens't kill my bandwidth)

# FactionsPlus version 0.4.4

- Fixed Permissions on /f unjail [player], permission: factionsplus.unjail

- Added command /f unsetjail, permission: factionsplus.unsetjail

- Added extraPowerWhenKillPlayer for real this time!

- Fixed bug with /f jail [player] where it would jail yourself

- Fixed security hole in /f jail [player] where you can jail players not in your Faction

- The configuration will update this time

- Removed useless code related to the DisguiseCraft API

- Fixed small mistake in the error text with /f warp

- Fixed a bug when the warp data file was being removed when you try to create a warp

# FactionsPlus version 0.4.3

- Faction Jails; yes they work now but only basic /f jail [player] and /f unjail [player] etc .. will add timed jails within time. Permission: factionsplus.jail

- Added /f setjail command, permission: factionsplus.setjail

- Huge code cleanup

- Added new aliases to createwarp:

  - addwarp

  - setwarp

- Added a new alias to removewarp:

  - unsetwarp

- Added support for DisguiseCraft

- Added support for MobDisguise

- Fixed bug with the permission factionsplus.listwarps

- Added new configuration options:

  - maxRulesPerFaction

  - extraPowerWhenKillPlayer

  - unDisguiseIfInEnemyTerritory

  - unDisguiseIfInOwnTerritory

  - extraPowerLossIfDeathBySuicide

  - extraPowerLossIfDeathByPVP

  - extraPowerLossIfDeathByMob

  - extraPowerLossIfDeathByCactus

  - extraPowerLossIfDeathByTNT

  - extraPowerLossIfDeathByFire

  - extraPowerLossIfDeathByPotion

  - enablePermissionGroups (currently not in use)

- You can now un-disguise a player according to the configuration options unDisguiseIfInEnemyTerritory, and unDisguiseIfInOwnTerritory

- Fixed bug with Peaceful Factions, and players leaving them not removing the power, in relation to peaceful bonus

- Added the command /f fc to go into Faction Chat, with the permission factionsplus.factionchatcommand

- Added the command /f gc to go into Public/Global Chat, with the permission factionsplus.globalchatcommand

- Bunch of small, minor, bug fixes

# FactionsPlus version 0.4.3

- Fixed a bug with /f warp

- Removed a bunch of left over ugly disgusting looking debug code

# FactionsPlus version 0.4.1

(this version was removed after being released)

- Added configuration options:

  - economy_costToToggleDownPeaceful

  - economy_costToToggleUpPeaceful

  - leadersCanFactionBan

  - officersCanFactionBan

  - leadersCanToggleState

  - officersCanToggleState

  - membersCanToggleState

- Added command /f togglestate <faction>

- Added command /f ban [player]

- Added the option to put passwords on warps using /f createwarp [name] <password>

- Allowed using warps with /f warp [name] <password>

- Fixed up configuration creation, and updating

- Fixed a horrible configuration problem I hope no one ever discovers

- Fixed up some event validation stuff related to canceling

- Some general cleanup in the code (expect a massive cleanup in the next release!)

# FactionsPlus version 0.4

- Economy working, using same method as Factions plugin

- Added possibility to deny commands in WarZone

- Made the Announcements more useful by allowing them to be shown to Faction members on login, and when they enter their land

- Added puff of smoke on teleport effect

- Added new configuration options: 
    - smokeEffectOnWarp
    - powerBoostIfPeaceful
    - showLastAnnounceOnLogin
    - showLastAnnounceOnLandEnter
- Plenty of general code improvements 
- Completed some more help pages 
- NOTE: 1.6.x users: Board.getFactionAt is picky in 1.6.x, and therefore the config option showLastAnnounceOnLandEnter is not working in this version! Expect 0.4b to fix this.

# FactionsPlus version 0.3c
- Fixed issue with announcements 

# FactionsPlus version 0.3b
- Fixed infinite loop on new configuration creation 

# FactionsPlus version 0.3
- Templates file is now being used REMOVE CURRENT TEMPLATE FILE
- Announcements now using templates.yml 
- Removed a bunch of debugging information 
- Removed more useless code related to checking if the player is a player (Factions does it for us!)
- Added Faction Home command /f factionhome [tag]
- If a warp doesn't exist, it tells you now
- Support for both 1.6.7 and 1.7.4 
- Announcements bug fixed with permissions etc

# FactionsPlus version 0.2
- Fixed bug with duplicate records in warps file
- Fixed a small problem with the use of File.separator
- Configuration works now, and will update automatically
- Introduced "mustBeInOwnTerritoryToCreate" configuration option + a lot more
- Supports Factions 1.6.x as well as 1.7.x now (I think, I hope)
- Added checks for admin/mod (support for 1.6.x and 1.7.x of Factions)
- Faction Announcements now check for ranking 
- Removed a bunch of useless code related to checking if player is a player (Factions plugin does it for us)
- Economy is approaching us, but leave it disabled for now (too buggy)
- Templates file introduced (but not yet used)
- Announcements can be modified via templates file
- Permissions!

# FactionsPlus version 0.1
- Initial Release 
- My first plugin release ever
- Faction Warps
- Faction Announcements
(source for these: pre 0.4.6 changelog lines was: http://dev.bukkit.org/server-mods/factionsplus/files.rss )
 