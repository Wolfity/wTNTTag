package me.wolf.wtnttag.arena;

import me.wolf.wtnttag.TNTTagPlugin;
import me.wolf.wtnttag.player.TNTPlayer;
import me.wolf.wtnttag.utils.CustomLocation;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Arena {

    private final TNTTagPlugin plugin;

    private final String name;
    private ArenaState arenaState = ArenaState.READY;
    private CustomLocation waitingRoomLoc;
    private FileConfiguration arenaConfig;
    private CustomLocation spawnLocation; // I will temporarily assign the arena spawn to the player's spawn point so it won't throw a null
    private final Set<TNTPlayer> arenaMembers = new HashSet<>();


    private int lobbyCountdown;
    private int gameTimer;
    private int explosionCountdown;
    private final int minPlayer;
    private final int maxPlayers;


    public File arenaConfigFile;

    protected Arena(final String name, final int gameTimer, final int lobbyCountdown, final int explosionCountdown, final int minPlayer, final int maxPlayers, final TNTTagPlugin plugin) {
        this.plugin = plugin;
        this.name = name;
        createConfig(name);
        this.gameTimer = gameTimer;
        this.lobbyCountdown = lobbyCountdown;
        this.minPlayer = minPlayer;
        this.maxPlayers = maxPlayers;
        this.explosionCountdown = explosionCountdown;

    }


    public void saveArena() {

        try {
            arenaConfig.set("LobbySpawn", waitingRoomLoc.serialize());
            arenaConfig.set("SpawnLocation", spawnLocation.serialize());
        } catch (final NullPointerException ignored) {

        }


        try {
            arenaConfig.save(arenaConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConfig(final String cfgName) {
        arenaConfigFile = new File(plugin.getDataFolder() + "/arenas", cfgName.toLowerCase() + ".yml");
        arenaConfig = new YamlConfiguration();
        try {
            arenaConfig.load(arenaConfigFile);
            arenaConfig.save(arenaConfigFile);
        } catch (IOException | InvalidConfigurationException ignore) {

        }
        if (!arenaConfigFile.exists()) {
            arenaConfigFile.getParentFile().mkdirs();
            try {
                arenaConfigFile.createNewFile();
                arenaConfig.load(arenaConfigFile);
                arenaConfig.set("min-players", 3);
                arenaConfig.set("max-players", 8);
                arenaConfig.set("lobby-countdown", 10);
                arenaConfig.set("explosion-countdown", 20);
                arenaConfig.set("game-timer", 600);
                arenaConfig.save(arenaConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }


    public void decrementGameTimer() {
        gameTimer--;
    }

    public void decrementLobbyCountdown() {
        lobbyCountdown--;
    }

    public void resetGameTimer() {
        this.gameTimer = arenaConfig.getInt("game-timer") + 1;
    }

    public void resetLobbyCountdownTimer() {
        this.lobbyCountdown = arenaConfig.getInt("lobby-countdown") + 1;
    }

    public void resetExplosionCountdownTimer() {
        this.explosionCountdown = arenaConfig.getInt("explosion-countdown") + 1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return name.equals(arena.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public ArenaState getArenaState() {
        return arenaState;
    }

    public CustomLocation getWaitingRoomLoc() {
        return waitingRoomLoc;
    }

    public FileConfiguration getArenaConfig() {
        return arenaConfig;
    }

    public CustomLocation getSpawnLocation() {
        return spawnLocation;
    }

    public Set<TNTPlayer> getArenaMembers() {
        return arenaMembers;
    }

    public int getLobbyCountdown() {
        return lobbyCountdown;
    }

    public int getGameTimer() {
        return gameTimer;
    }

    public File getArenaConfigFile() {
        return arenaConfigFile;
    }

    public void setWaitingRoomLoc(final CustomLocation waitingRoomLoc) {
        this.waitingRoomLoc = waitingRoomLoc;
    }

    public void setSpawnLocation(final CustomLocation customLocation) {
        this.spawnLocation = customLocation;
    }

    public void setArenaState(final ArenaState arenaState) {
        this.arenaState = arenaState;
    }


    public int getExplosionCountdown() {
        return explosionCountdown;
    }

    public void decrementExplosionCountdown() {
        explosionCountdown--;
    }

}