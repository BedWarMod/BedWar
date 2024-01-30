# BedWar Mod - Change Log

## Version 0.1.6
- Stopped some npc messages being detected as player messages
- Added `/pd` to short party commands
- Added party kick with reason so you can do `/pk calmwolfs low fkdr`
- And everything from 0.1.6.Pre 1, 2 and 3

## Version 0.1.6.Pre.3
- Fixed another small typo causing death notifications to not use the correct settings

## Version 0.1.6.Pre.2
- Fixed some bugs with team kill and death notifications causing it to not work as intended

## Version 0.1.6.Pre.1
- Allowed you to use old api system as a backup in case the current one is down or cannot be accessed
- Add team death notifications that show when your teammates are killed or final killed

## Version 0.1.5
- Allow customisable notification duration
- Improved repo error logging
- Used my own api backend so you no longer need a key
- Some other small improvements
- Fixed game notifications not being able to be disabled
- And everything from 0.1.5.Pre-1

## Version 0.1.5.Pre.1
- Potentially fixed null pointer error due to tablist not having a header or footer
- Fixed notification having wrong dimensions when leaving if a new one came
- Small wording fixes

## Version 0.1.4
- Added notifications - Displays for various things, and they can be customised in multiple ways
- Party Join notification and notifications when someone on your team gets a kill or breaks a bed
- Fixed bug where party stats would not send your stats at the end of a game
- Made some things round less frequently plus some more formatting changes
- Show kills/finals/beds per game in `/bws`
- Replace `/l1` with `/swaplobby 1`
- Fix potential repo issues
- Some more small performance issues fixed
- Some slight improvements to party chat complete
- Made the mod work in swap game mode
- Fixed not being able to edit session tracker if team status was showing

## Version 0.1.3
- Trap display - shows what traps your team currently has
- Actually detect teammates as eliminated
- Added some more colours based on health to the team status overlay
- Allowed the user to customise the delay before the parties stats are sent to chat
- Made the mod not run when not in a BedWars area
- Fetch your winstreak from the npc in the hub for the session display
- Made party match stats work for nicked teammates (but will show them twice)
- Fixed clicking teammates messages in chat not running `/bws`
- Reworded config options to make a bit more sense
- Made features that need an api key not work if none is set in the mod

## Version 0.1.2
- Teammate status display - Shows how much health each of your teammates has along with whether they have been eliminated or disconnected
- BedWars star colour should now reflect Hypixel colours in `/bws`
- Made the mod work while in lucky block and allows for repo updates for other weird start messages
- Various bug fixes
- Formatting improvements for stats features
- A few changes that should theoretically improve performance

## Version 0.1.1
- Made all the chat events work with the repo
- Change some wording of features
- Tab complete in BedWars games now bases the players off of when the game started

## Version 0.1
- Setup GitHub repository
- Setup project - Auto update, fetching from repo, config menu
- Resource overlay
- Middle click in shops
- Highlight things you can and can't purchase in shops
- Customisable session stat display
- Party match stats
- Copy and send match stats
- Party commands shortening and tab completing
- Ping on chat mention
- Copy chat messages
- `/bws <player>` command
- See stats of players
- Can switch between game modes
- Auto send on player joining your party