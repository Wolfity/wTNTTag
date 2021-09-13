package me.wolf.wtnttag;

import me.wolf.wtnttag.arena.Arena;
import me.wolf.wtnttag.arena.ArenaManager;
import me.wolf.wtnttag.commands.impl.TNTTagCommand;
import me.wolf.wtnttag.game.GameListeners;
import me.wolf.wtnttag.game.GameManager;
import me.wolf.wtnttag.listeners.*;
import me.wolf.wtnttag.player.TNTPlayer;
import me.wolf.wtnttag.scoreboard.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class TNTTagPlugin extends JavaPlugin {

    private ArenaManager arenaManager;
    private GameManager gameManager;
    private Scoreboards scoreboard;


    private final Set<Arena> arenas = new HashSet<>();
    private final Map<UUID, TNTPlayer> tntPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        final TNTTagPlugin plugin = this;

        File folder = new File(plugin.getDataFolder() + "/arenas");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        registerCommands();
        registerListeners();
        registerManagers();

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        arenaManager.saveArenas();


    }

    private void registerCommands() {
        Collections.singletonList(
                new TNTTagCommand(this)
        ).forEach(this::registerCommand);

    }

    private void registerListeners() {
        Arrays.asList(
                new GameListeners(this),
                new PlayerQuit(this),
                new BlockBreak(this),
                new BlockPlace(this),
                new EntityDamage(this),
                new InventoryListener(this),
                new FoodChange(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerManagers() {
        this.arenaManager = new ArenaManager(this);
        this.arenaManager.loadArenas();
        this.gameManager = new GameManager(this);
        this.scoreboard = new Scoreboards(this);

    }

    private void registerCommand(final Command command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Scoreboards getScoreboard() {
        return scoreboard;
    }

    public Set<Arena> getArenas() {
        return arenas;
    }

    public Map<UUID, TNTPlayer> getTntPlayers() {
        return tntPlayers;
    }


}
