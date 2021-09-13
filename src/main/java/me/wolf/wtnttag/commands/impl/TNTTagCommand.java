package me.wolf.wtnttag.commands.impl;

import me.wolf.wtnttag.TNTTagPlugin;
import me.wolf.wtnttag.arena.Arena;
import me.wolf.wtnttag.commands.BaseCommand;
import me.wolf.wtnttag.constants.Messages;
import me.wolf.wtnttag.game.GameState;
import me.wolf.wtnttag.utils.CustomLocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TNTTagCommand extends BaseCommand {

    private final TNTTagPlugin plugin;

    public TNTTagCommand(final TNTTagPlugin plugin) {
        super("tnt");
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {

        final Player player = (Player) sender;

        if (args.length < 1 || args.length > 2) {
            tell(Messages.HELP);
        }
        if (isAdmin()) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("setspawn") || args[0].equalsIgnoreCase("setlobby")
                        || args[0].equalsIgnoreCase("createarena") || args[0].equalsIgnoreCase("deletearena")) {
                    tell("&bPlease specify an arena!");
                }
                if (args[0].equalsIgnoreCase("admin")) {
                    tell(Messages.ADMIN_HELP);
                } else if (args[0].equalsIgnoreCase("setworldspawn")) {
                    plugin.getConfig().set("WorldSpawn", player.getLocation());
                    plugin.saveConfig();
                    tell(Messages.SET_WORLD_SPAWN);
                }
            } else if (args.length == 2) {
                final String arenaName = args[1];
                if (args[0].equalsIgnoreCase("createarena")) {
                    plugin.getArenaManager().createArena(arenaName);
                    tell(Messages.ARENA_CREATED.replace("{arena}", arenaName));
                } else if (args[0].equalsIgnoreCase("deletearena")) {
                    if (plugin.getArenaManager().getArena(arenaName) != null) {
                        plugin.getArenaManager().deleteArena(arenaName);
                        tell(Messages.ARENA_DELETED.replace("{arena}", arenaName));
                    } else tell(Messages.ARENA_NOT_FOUND);
                } else if (args[0].equalsIgnoreCase("setlobby")) {
                    setArenaLobby(player, arenaName);
                } else if (args[0].equalsIgnoreCase("setspawn")) {
                    setGameSpawn(player, arenaName);
                } else if (args[0].equalsIgnoreCase("forcestart")) {
                    plugin.getGameManager().setGameState(GameState.LOBBY_COUNTDOWN, plugin.getArenaManager().getArena(arenaName));
                } else if (args[0].equalsIgnoreCase("tp")) {
                    if (plugin.getArenaManager().getArena(arenaName) != null) {
                        player.teleport(plugin.getArenaManager().getArena(arenaName).getWaitingRoomLoc().toBukkitLocation());
                        tell(Messages.TELEPORTED_TO_ARENA);
                    } else tell(Messages.ARENA_NOT_FOUND);
                }
            }
        }

        if (args.length == 2) {
            final String arenaName = args[1];
            if (args[0].equalsIgnoreCase("join")) {
                if (plugin.getArenaManager().getArena(arenaName) != null) {
                    plugin.getGameManager().addPlayer(player, plugin.getArenaManager().getArena(arenaName));
                }
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("leave")) {
                plugin.getGameManager().removePlayer(player);
            } else if (args[0].equalsIgnoreCase("join")) {
                tell("&bPlease specify an arena!");
            } else if (args[0].equalsIgnoreCase("help")) {
                tell(Messages.HELP);
            }
        }
    }

    // setting game spawns and saving them in the arena config
    private void setGameSpawn(final Player player, final String arenaName) {
        if (plugin.getArenaManager().getArena(arenaName) == null) {
            tell(Messages.ARENA_NOT_FOUND);
        }
        final Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (plugin.getArenaManager().isGameActive(arena)) {
            tell(Messages.CAN_NOT_MODIFY);
        }

        arena.getArenaConfig().set("SpawnLocation", player.getLocation().serialize());
        arena.setSpawnLocation(CustomLocation.fromBukkitLocation(player.getLocation()));

        tell(Messages.SET_GAME_SPAWN);
        player.getWorld().save();
        arena.saveArena();
    }

    // setting an arena's waiting room lobby location
    private void setArenaLobby(final Player player, final String arenaName) {
        if (plugin.getArenaManager().getArena(arenaName) == null) {
            tell(Messages.ARENA_NOT_FOUND);
        }
        final Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (plugin.getArenaManager().isGameActive(arena)) {
            tell(Messages.CAN_NOT_MODIFY);
        }
        arena.getArenaConfig().set("LobbySpawn", player.getLocation().serialize());
        arena.setWaitingRoomLoc(CustomLocation.fromBukkitLocation(player.getLocation()));
        player.getWorld().save();
        arena.saveArena();
        tell(Messages.SET_LOBBY_SPAWN);
    }
}
