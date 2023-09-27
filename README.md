<h1 align="center">BedWar Mod</h1> 

<div align="center">
    <!-- release -->
    <a href="https://github.com/BedWarMod/BedWar/releases/latest" target="_blank">
        <img src="https://img.shields.io/github/v/release/BedWarMod/BedWar?color=informational&include_prereleases&label=release&logo=github&logoColor=white" alt="release">
    </a>
    <!-- license -->
    <a href="./LICENSE" target="_blank">
        <img src="https://img.shields.io/github/license/BedWarMod/BedWar?color=informational" alt="license">
    </a>
    <!-- contributors -->
    <a href="https://github.com/BedWarMod/BedWar/graphs/contributors" target="_blank">
        <img src="https://img.shields.io/github/contributors/BedWarMod/BedWar?color=informational&logo=GitHub" alt="contributors">
    </a>
    <!-- downloads -->
    <a href="https://github.com/BedWarMod/BedWar/releases" target="_blank">
        <img src="https://img.shields.io/github/downloads/BedWarMod/BedWar/total?label=downloads&color=informational&logo=GitHub" alt="downloads">
    </a>
</div>

## About

BedWar Mod is a mod for Minecraft 1.8.9 Forge. It provides many features to be used while playing Hypixel Bedwars

This mod is still in development, but if you want to test it, it is available. There will be some bugs, but they should 
not impact gameplay too much. If you find any bugs or have any suggestions feel free to report them through the GitHub issues.

Currently, this mod does not use Hypixel's new API system but that will be changed soon. It only makes API requests when 
you run `/bws <player>`. This will be updated to a new system in the coming weeks.

Also feel free to contribute to the mod!

## Features
All features are toggleable and should not introduce any lag or break other mods

#### General
* Auto Updater - Be notified about updates in-game

#### Inventory
* Resource Overlay - See what resources you have in your inventory and in your ender chest
* Shop Inventory Overlay - Highlight items in the shop based on whether you can afford them or if they are already bought/maxed
* Middle Clip In Shops - Replace your left/right click with middle click to stop you from picking up the item

## Session Display
* Shows Stats From Current Session - Keep track of stats from the current session e.g. Final Kills, KDA, average game time
* Fully Customisable - The order of the list can be fully changed and anything in the list can be removed if you don't want it

## Party
* Party Match Stats - At the conclusion of the game display how many kills, finals and beds each party member got
* Copy or Send Stats - Option for at the conclusion of the game to either copy your stats into the clipboard or send it 
* to the party in the format `kills finals beds` e.g. `5 4 1`
* Party Commands - Shortens commands such as `/pt` as `/p transfer`. Also allows tab completion for anyone in your party 
* for party commands and can tab complete for inviting someone to your party

## Chat
* Copy Chat - Upon control-clicking a message in chat, the mod will copy that chat line to your clipboard
* Chat Mentions - Options to hear a ding when someone types your name in chat along with highlighting your name in chat

## API
* Player Stats - Upon clicking someone's message in chat or doing `/bws <player>` you will see their BedWars stats.
* Stat Mode - Change the default BedWars stat mode that is sent in chat.
* Send On Party Join - When someone joins your party their stats will automatically be sent in your chat
## Getting Started

#### Installing Forge

1. Run normal Minecraft 1.8.9 and once it reaches the title screen wait about 5 seconds and close it.
2. Install Minecraft **1.8.9** forge from the [forge website](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.8.9.html)
    - Once you click on the installer you'd like to download, a window will pop up. **Do not click on anything in the middle of 
    - your screen**; instead, click on the `skip ad` button towards the top right
3. Open the installer, select install client, and click install
4. When forge is installed, open the Minecraft launcher, go under the `installations tab`, click `new installation`, select the version 
5. release `1.8.9-forge1.8.9-11.15.1.xxxx` (it will usually be all the way towards the bottom).
6. Once you are done, run this new installation that you just created. Once it reaches the title screen, wait about 5 seconds and close it.

#### Installing the Mod

1. Download the latest mod [release](https://github.com/BedWarMod/BedWar/releases/latest). If it says `this file may harm your computer`, click `allow anyways` as all java files will be flagged by Chrome.
2. Add the mod to forge:
    Press the Windows key + R; type `%appdata%`; click on the folder called `.minecraft`; click on the folder called `mods` and drag the mods file in here.
3. Open the Minecraft launcher and run your forge installation you set up earlier.
4. Type `/bw`. If you see the BedWar Mod menu, you have done this correctly!

## Other Recommended Mods
There are many other mods that I would recommend to use in addition to BedWar Mod so that you can have the best pvp experience while playing Hypixel BedWars.
Through these mods you can get all the same features as popular clients such as Lunar or Badlion while also having access to many more mods (such as this one).
You will also get the same performance if not better while using forge and the mods below.

#### [EverGreen Hud](https://www.curseforge.com/minecraft/mc-mods/evergreenhud)

A Mod that provides many different useful gui displays such as your armor

#### [Hytils Reborn](https://github.com/Polyfrost/Hytils-Reborn)

This mod provides many generic features for all of Hypixel such as height limit overlay and cleaner chat with spam removed

#### [OverFlowAnimations](https://github.com/Polyfrost/OverflowAnimationsV2)

Provides many old 1.7 animations such as block hitting

#### [Patcher](https://sk1er.club/mods/patcher)

Patcher fixes many bugs in Minecraft 1.8.9 along with providing massive FPS boosts

#### [OptiFine](https://www.optifine.net/downloads)

OptiFine also boosts your FPS a lot along with allowing you to zoom in when pressing a key on your keyboard


If any mods are missing from here feel free to recommend them!

## License

This project is currently licensed under LGPL-3.0, see [LICENSE](LICENSE) for more details.

#### Contribution licensing

Contributions to this repository will be automatically licensed under the licensing for the repository at that time (currently version 3 of the LGPL), as defined in Subsection D6 of the GitHub Terms of Service.