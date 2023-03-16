# WynnLibFabric
WynnLib is a Fabric mod that aims to provide data-related functionalities for the server Wynncraft.
The mod inherits from the mod WynnInvManager (WIM), 
but comes up with a better framework design and supports newer Minecraft versions. 
As an open-source project, the contribution is welcome. 

Feel free to use the mod as a data library of your own mod. 

# Install Guide
There are two ways to download the mod Jar file: 
1. From GitHub action (CI) which will build new jar for every new update. Note that you need a GitHub account in order to download it. 
   https://github.com/nbcss/WynnLibFabric/actions
2. From the Modrinth, which will build manually by me for each stable release.
   https://modrinth.com/mod/wynnlib

Note that WynnLib does not have auto-update implemented, you will need to check new update by yourself and download new Jar each time in order to update.

The mod require Fabric 1.18 to run, and currently will not support Forge. 
In addition, the following dependency mods have to be installed as well: 
- Fabric API: https://www.curseforge.com/minecraft/mc-mods/fabric-api
- Fabric Language Kotlin: https://www.curseforge.com/minecraft/mc-mods/fabric-language-kotlin

Put the jar files of all dependency mod and this mod into mods folder, and start the game.

# TODO LIST
- [x] Item Dictionary (Equipment, Ingredient, Material, Powder)
- [x] Dictionary item name filter
- [x] Ability Tree Viewer/Builder/Editor
- [x] Ability Indicator (Timer)
- [x] Item Analysis (Partially works)
- [ ] Custom Indicators
- [ ] Advance Search Panel
- [ ] Crafter
- [ ] Builder
- [ ] Configuration Page

# Development Guide
The current main development branch is 1.18,
please work on this branch to add new features.
In addition, please join the Discord channel TmW4mB8 
and say what features you are trying to add before doing it.

# License
This mod is licensed under the [AGPL-v3.0](https://www.gnu.org/licenses/agpl-3.0.en.html) license.

All assets in the mod belong to their contributer and are licensed under the [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/) license.

# Contributors
Code: 
- nbcss
- FYWind

Database:
- Raw_Fish

Art: 
- Silentmaxx

Localization: 
- Moe_block
- Dexmio
- Puriora
- ShoeBox_CX
- LuCoolUs
- Fudy

For localization team: 
Contact nbcss if your name is not shown here. 
For atree or item issues, contact RawFish or nbcss
