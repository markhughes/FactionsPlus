# FactionsPlus 0.7.0

* Huge code clean up
* Fixed a small issue with not ensuring FactionsPlusUpdate was not running
* Now using MassiveCore
* config.yml no longer used, now using mstore
* templates are now referred to as languages, and is under LConf - all FactionsPlus texts are now editable! 
* LWC features removed - now all handled by Factions
* Added permission factionsplus.allowregionclaim.<id>
* WorldGuard region check now also checks owners
* Updates notify both ops and players in admin mode
* Power Settings command now works again, and shows more information
* Can now ignore need requests with /f need listen/ignore 
* Added /f scoreboard/sb command 
* Listeners completely re-organised into sublisteners 
* Added support for Magic Plugin - although the Magic plugin adds integration, this is much feature packed! 
* Added configuration option magicDisallowInEnemy
* Added configuration option magicDisallowInOwn
* Added configuration option magicDisallowInSafeZone
* Added configuration option magicDisallowInWarZone
* Added configuration option magicDisallowInFactions
* Added configuration option magicRequiresMinimumPowerOf
* Added command /f chest
* Added permission factionsplus.chest
* Added permission factionsplus.chest.set
* Added interesting new metrics 