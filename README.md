# wTNTTag
a TNT Tag minigame

### **Introduction**
TNT Tag is a minigame where several players get selected as a tagger. 
When you are the tagger, you have got TNT in your inventory. 
To get rid of the TNT, you have to tag someone. 
Taggers get speed three while non-taggers get a speed two effect. 
After the explosion countdown ends, the taggers will explode, and new taggers will be selected. 

### **How to set it up**
**World Spawn**\
The first thing you have to do is create a world spawn. 
You can do that by typing the command [/tnt setworldspawn]. 
Players will be teleported to that location if they leave the game or if the game ends.

**Lobby and Game Spawns**\
After you create the world spawn, you will have to make a lobby spawn point. 
You can do that by executing the command [/tnt setlobby arenaName]. 
All that’s left to do now is set a game spawn.
You can do that by using the command [/tnt setspawn arenaName].

### **Commands**
`/tnt createarena <arenaName>` Creates a new arena\
`/tnt deletearena <arenaName>` Deletes an existing arena\
`/tnt join <arenaName>` Join an arena\
`/tnt leave` Leaves an arena\
`/tnt setlobby <arenaName>` Sets the lobby spawn point for an arena\
`/tnt setspawn <arenaName>` Sets a game spawn point for an arena\
`/tnt setworldspawn` Sets the world spawn, the spawn where players go to after the game ends, or they leave\
`/tnt help` Displays the help message\
`/tnt admin` Displays the admin message\
`/tnt tp <arenaName>` Teleports you to the arena
 