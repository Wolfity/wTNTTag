package me.wolf.wtnttag.arena;

import me.wolf.wtnttag.TNTTagPlugin;
import me.wolf.wtnttag.player.TNTPlayer;
import me.wolf.wtnttag.utils.CustomLocation;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Objects;


public class ArenaManager {

    private final TNTTagPlugin plugin;

    public ArenaManager(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }


    public Arena createArena(final String arenaName) {
        for (final Arena arena : plugin.getArenas())
            if (arena.getName().equalsIgnoreCase(arenaName))
                return getArena(arenaName);

        final Arena arena = new Arena(arenaName, 200, 10, 20, 3, 8, plugin);
        plugin.getArenas().add(arena);
        return arena;
    }

    public void deleteArena(final String name) {
        final Arena arena = getArena(name);
        if (arena == null) return;

        arena.getArenaConfigFile().delete();
        plugin.getArenas().remove(arena);
    }


    // get an arena by passing in it's name
    public Arena getArena(final String name) {
        for (final Arena arena : plugin.getArenas())
            if (arena.getName().equalsIgnoreCase(name))
                return arena;

        return null;
    }

    // getting an arena by passing in a player, looping over all arenas to see if the player is in there
    public Arena getArenaByPlayer(final TNTPlayer tntPlayer) {
        for (final Arena arena : plugin.getArenas()) {
            if (arena.getArenaMembers().contains(tntPlayer)) {
                return arena;
            }
        }
        return null;
    }

    public boolean isGameActive(final Arena arena) {
        return arena.getArenaState() == ArenaState.INGAME ||
                arena.getArenaState() == ArenaState.END ||
                arena.getArenaState() == ArenaState.COUNTDOWN;
    }

    public void loadArenas() {
        final File folder = new File(plugin.getDataFolder() + "/arenas");

        if (folder.listFiles() == null) {
            Bukkit.getLogger().info("&3No arenas has been found!");
            return;
        }


        for (final File file : Objects.requireNonNull(folder.listFiles())) {
            final Arena arena = createArena(file.getName().replace(".yml", ""));

            arena.setWaitingRoomLoc(CustomLocation.deserialize(arena.getArenaConfig().getString("LobbySpawn")));
            arena.setSpawnLocation(CustomLocation.deserialize(arena.getArenaConfig().getString("SpawnLocation")));

            Bukkit.getLogger().info("&aLoaded arena &e" + arena.getName());

        }
    }

    public void saveArenas() {
        for (final Arena arena : plugin.getArenas()) {
            arena.saveArena();
        }
    }


}
