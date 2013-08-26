# FactionsPlus 0.6.6
- Add permission factionsplus.bypassregioncheck to allow bypassing worldguardCanBuildCheck
- Added support for essentials /eback in /back denying 
- Fixed a bug with Essentials and teleporting to a home without an existing world
- Fixed addwarps /f help, added its description, and optional argument (password)

# FactionsPlus 0.6.5
- Fixes a bug where you can't interact with any animals
- Added configuration option `safeZonesExtraSafe`. (Resolves issue #52)
- Code cleanup and general enhancements 
- Fixed faction_need template 
- Now runs unregister on SB if they aren't supposed to have it
- Added new configuration option worldguardCanBuildCheck
- LWC Protection removal for Hoppers added
- Removed /f powsets

# FactionsPlus version 0.6.4
- Fixed a bug where the client would think you're getting mushroom stew from mooshroom cow - but you really didn't ;) 
- allowFactionKillNeutralMobs no longer affects Wilderness (woo!) 
- FType improvements - more productive 
- New 2.0.0 addPower means instead of power boosts, we assign power! 
- I've attempted to install a system to use both new and old version of Essentials. I suggest using Essentials-2.11.1
- Damage checks with allowFactionKill*Mobs now includes fishing hooks
- Fixed an issue with people in the wilderness/none faction can create announcements, and use some FactionsPlus commands
- Scoreboards now include Power Boost, and Land Power 
- Scoreboards are now sent to players when they login 
- Fixed scoreboards to support up to 999999 power (wa-hoo!)
- allowShopsInWilderness is now in use (ChestShop)
- allowShopsInTerritory is now in use (ChestShop)
- New configuration option protectSafeAnimalsInSafeZone, which will do the same as allowFactionKill*Mobs but in SafeZones 
- Removal of leaderCanNotBeBanned, not needed. Leaders should never be banned from their own Faction. 
- allowShopsInWilderness / allowShopsInTerritory also supports ShowCaseStandalone 

# FactionsPlus version 0.6.3
- Further advancements on FType (turns out pernament factions can have leaders if setup that way)
- Fixed a bug for scoreboards when the Faction name was larger than 14 characters, now accepts the 16 character limit
- Introduction of FactionsPlusLink (a developer thing, nothing to worry about!)
- Added new configuration option delayBeforeSentToJail, you can now delay (in seconds) before how long players go to jail
	This will fix situations where a player is in combat, and they get someone to jail them to avoid dying. 
- Fixed a bug when fetching a Faction in /f factionhome
- Now compatiable with with other scoreboard-related plugins
- Better management of Scoreboards in general 
- Scoreboards will now return correct power 
- Scoreboards can be configure between seconds now
- Added the ability to deny access to killing 'safe' (farming-style) mobs in Faction lands (warning: not very tested),
These being: Chicken, Cow, Mushroom Cow, Ocelot, Wolf, Pig, Iron Golem, Bat, Snowman, Villager, Horse, or Squid
- Added permission factionsplus.cankillallmobs
- Added new configuration option: extras -> protection -> allowFactionKillEnemyMobs
- Added new configuration option: extras -> protection -> allowFactionKillEnemyMobs
- Added new configuration option: extras -> protection -> allowFactionKillNeutralMobs
- Introduced stricter farming (through stricterFarmingProtection) which will deny farming (Milking, Fishing, Shearing) in WarZones and other Factions land
- Added new configuration option: extras -> protection -> stricterFarmingProtection
- Added support onto allowFactionKill*Mobs for Arrows, Snowballs, Thrown Potions, Ender Pearls, Eggs, and Fireballs.
- Implements a fix to stop stealing horses / pigs and interacting with them when you're not apart of the Faction land that it's in
- Added new configuration option: extras -> protection -> onlyPeacefulCreateLWCProtections (as per ticket 25 on dev.bukkit)
- Added new configuration option: Factions -> factionNameFirstLetterForceUpperCase (as per ticket 53 on dev.bukkit)
- General stability enhancments and productiveness
- Fixed bug with getting UPlayer object in Jails (2.0.0 migration issue, requires Player object not a name String) 

# FactionsPlus version 0.6.2
- Fixed alias for /f need
- Added text for /f need to templates file
- Added notice for when Essentials is not updated and trying to run Essentials integration stuff (ClassNotFound errors)
- Ops are now messaged on join if there is an update for FactionsPlus
- SafeZones and WarZones should be 100% fine now, introduced new FType system
- When warping, and the Faction that you're teleporting to doesn't have a warp - it will now make more sense as to what you're doing 
(e.g. if you're not in a Faction it will say, you're not in a Faction - not that your faction has no warps,
if you're trying to warp to another Factions warp that has no warps it will say that Faction has no warps, not that your Faction has no warps,
etc.)
- Essentials has reverted back their getSafeDestination in Essentials-Pre2.11.1.6. Please use Essentials-Pre2.11.1.6. 
- Small issue with Scoreboards fixed

# FactionsPlus version 0.6.1
- Introduction of Score Board, enabled with extras.enableScoreBoardOfFactions
- New permission: factionsplus.hidesb can be used to hide the scoreboard per permission if it is enabled on the server
- New permission: factionsplus.hidesb.WORLD_NAME can be used to hide the scoreboard per world name
- Introduction of /f need. Will announce to faction leaders and officers that the player needs a faction.
- New permission: factionsplus.need required to use /f need
- Fixed an issue with Warzone / Safezone / Wilderness where being incorrect detected due to the new ID system 
- Updated Metrics to R7
- Added permission factionsplus.forcesb (more-so for ops, as all their permissions return TRUE so factionsplus.hidesb will return TRUE)

# FactionsPlus version 0.6.0

- Translation of everything into Factions 2.0.0
- Removal of old 1.6 - 1.8 code, including:
- Complete removal of old Faction Chat stuff
- Removal of bridge between 1.6 and 1.7/1.8
- Started to add support for Score Board (incomplete). To use this, enable extras.enableScoreBoardOfFactions
- Updated dev-url to the yet again changed URL