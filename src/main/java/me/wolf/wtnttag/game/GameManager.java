package me.wolf.wtnttag.game;

import me.wolf.wtnttag.TNTTagPlugin;
import me.wolf.wtnttag.arena.Arena;
import me.wolf.wtnttag.arena.ArenaState;
import me.wolf.wtnttag.constants.Messages;
import me.wolf.wtnttag.player.TNTPlayer;
import me.wolf.wtnttag.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class GameManager {

    private final TNTTagPlugin plugin;

    public GameManager(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }

    private GameState gameState;

    public void setGameState(final GameState gameState, final Arena arena) {
        this.gameState = gameState;
        switch (gameState) {
            case RECRUITING:
                arena.setArenaState(ArenaState.READY);
                enoughPlayers(arena);
                break;
            case LOBBY_COUNTDOWN:
                arena.setArenaState(ArenaState.COUNTDOWN);
                lobbyCountdown(arena);
                break;
            case ACTIVE:
                arena.setArenaState(ArenaState.INGAME);
                teleportToSpawns(arena);
                selectTaggers(arena);
                gameTimer(arena);
                break;
            case END:
                arena.setArenaState(ArenaState.END);
                sendGameEndNotification(arena);
                Bukkit.getScheduler().runTaskLater(plugin, () -> endGame(arena), 200L);
                break;
        }
    }

    // game timer
    private void gameTimer(final Arena arena) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState != GameState.ACTIVE) {
                    this.cancel();
                }
                if (arena.getGameTimer() > 0) {
                    arena.decrementGameTimer();
                    arena.getArenaMembers().
                            stream().
                            filter(Objects::nonNull).forEach(tntPlayer -> {
                        final Player player = Bukkit.getPlayer(tntPlayer.getUuid());
                        plugin.getScoreboard().gameScoreboard(player, arena);
                        player.setHealth(20);
                    });
                    explosionTimer(arena);
                    updateEffects(arena);
                } else {
                    this.cancel();
                    setGameState(GameState.END, arena);
                    arena.resetGameTimer();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // handles the lobby countdown timer
    private void lobbyCountdown(final Arena arena) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState != GameState.LOBBY_COUNTDOWN) {
                    this.cancel();
                }
                if (arena.getLobbyCountdown() > 0) {
                    arena.decrementLobbyCountdown();
                    arena.getArenaMembers().stream().filter(Objects::nonNull).forEach(tntPlayer -> {
                        final Player player = Bukkit.getPlayer(tntPlayer.getUuid());
                        player.sendMessage(Messages.LOBBY_COUNTDOWN.replace("{countdown}", String.valueOf(arena.getLobbyCountdown())));
                    });
                } else {
                    this.cancel();
                    arena.resetLobbyCountdownTimer();
                    arena.getArenaMembers().forEach(tntPlayer -> {
                        final Player player = Bukkit.getPlayer(tntPlayer.getUuid());
                        player.sendMessage(Messages.GAME_STARTED);
                    });
                    setGameState(GameState.ACTIVE, arena);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // clearing out objects, lists, teleporting players back, etc
    private void endGame(final Arena arena) {

        arena.getArenaMembers().stream().filter(Objects::nonNull).forEach(tntPlayer -> {

            final Player player = Bukkit.getPlayer(tntPlayer.getUuid());
            player.getInventory().clear();
            teleportToWorld(arena);

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.SURVIVAL);

            plugin.getTntPlayers().remove(player.getUniqueId());

            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            tntPlayer.setSpectator(false);
            tntPlayer.setTagger(false);
            player.setPlayerListName(player.getDisplayName());

            resetTimers(arena);
        });

        arena.getArenaMembers().clear();
        setGameState(GameState.RECRUITING, arena);
    }

    // check if there are enough players to start
    private void enoughPlayers(final Arena arena) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState == GameState.RECRUITING) {
                    if (arena.getArenaMembers().size() >= arena.getArenaConfig().getInt("min-players")) {
                        setGameState(GameState.LOBBY_COUNTDOWN, arena);
                    } else {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // after the game ends, players will be teleported back to the world spawn
    private void teleportToWorld(final Arena arena) {
        arena.getArenaMembers().forEach(tntPlayer -> {
            final Player player = Bukkit.getPlayer(tntPlayer.getUuid());
            final Location worldLoc = (Location) plugin.getConfig().get("WorldSpawn");
            player.teleport(worldLoc);
            plugin.getTntPlayers().remove(player.getUniqueId());
        });
    }


    private void teleportToSpawns(final Arena arena) {
        arena.getArenaMembers().forEach(tntPlayer -> Bukkit.getPlayer(tntPlayer.getUuid()).teleport(arena.getSpawnLocation().toBukkitLocation()));
    }

    public void teleportToLobby(final Player player, final Arena arena) {
        player.teleport(arena.getWaitingRoomLoc().toBukkitLocation());
    }

    // Adds a new player to a specific arena, creates the Custom Player object.
    public void addPlayer(final Player player, final Arena arena) {

        if (!plugin.getArenaManager().isGameActive(arena)) {
            if (!arena.getArenaMembers().contains(plugin.getTntPlayers().get(player.getUniqueId()))) {
                if (arena.getArenaMembers().isEmpty()) {
                    setGameState(GameState.RECRUITING, arena);
                }
                if (arena.getArenaMembers().size() <= arena.getArenaConfig().getInt("max-players")) {

                    plugin.getTntPlayers().put(player.getUniqueId(), new TNTPlayer(player.getUniqueId()));
                    final TNTPlayer tntPlayer = plugin.getTntPlayers().get(player.getUniqueId());
                    arena.getArenaMembers().add(tntPlayer);

                    plugin.getScoreboard().lobbyScoreboard(player, arena);
                    teleportToLobby(player, arena);
                    player.getInventory().clear();
                    arena.getArenaMembers().stream().filter(Objects::nonNull).forEach(arenaMembers -> {
                        final Player arenaPlayers = Bukkit.getPlayer(arenaMembers.getUuid());
                        plugin.getScoreboard().lobbyScoreboard(arenaPlayers, arena);
                        arenaPlayers.sendMessage(Messages.PLAYER_JOINED_GAME.replace("{player}", player.getDisplayName()));
                    });
                    enoughPlayers(arena);
                    player.sendMessage(Messages.JOINED_ARENA.replace("{arena}", arena.getName()));
                } else player.sendMessage(Messages.ARENA_IS_FULL);
            } else player.sendMessage(Messages.ALREADY_IN_ARENA);
        } else player.sendMessage(Messages.GAME_IN_PROGRESS);
    }

    // remove a player from the game, teleport them, clear the custom player object
    public void removePlayer(final Player player) {
        for (final Arena arena : plugin.getArenas()) {
            if (!arena.getArenaMembers().contains(plugin.getTntPlayers().get(player.getUniqueId()))) {
                player.sendMessage(Messages.NOT_IN_ARENA);
            }
            if (plugin.getConfig().get("WorldSpawn") == null) {
                player.sendMessage(Utils.colorize("&cSomething went wrong, no world spawn set!"));
            }

            final TNTPlayer tntPlayer = plugin.getTntPlayers().get(player.getUniqueId());
            arena.getArenaMembers().remove(tntPlayer);
            plugin.getTntPlayers().remove(player.getUniqueId());

            player.teleport((Location) plugin.getConfig().get("WorldSpawn"));
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            player.getInventory().clear();
            player.sendMessage(Messages.LEFT_ARENA.replace("{arena}", arena.getName()));
            player.getInventory().clear();
            leaveGameCheck(arena);
            arena.getArenaMembers()
                    .stream().filter(Objects::nonNull)
                    .forEach(arenaMember -> Bukkit.getPlayer(arenaMember.getUuid()).sendMessage(Messages.PLAYER_LEFT_GAME.replace("{player}", player.getDisplayName())));
        }
    }

    // If someone leaves, check if there are any players left, else reset the game to a specific state
    private void leaveGameCheck(final Arena arena) {
        if (gameState == GameState.ACTIVE) {
            if (arena.getArenaMembers().size() <= 1) {
                setGameState(GameState.END, arena);
                resetTimers(arena);
            }
        } else if (gameState == GameState.LOBBY_COUNTDOWN) {
            if (arena.getArenaMembers().size() <= 1) {
                setGameState(GameState.RECRUITING, arena);
                resetTimers(arena);
            }
        }
    }

    // sends out the notification that the game has ended
    private void sendGameEndNotification(final Arena arena) {
        arena.getArenaMembers().stream().filter(Objects::nonNull).forEach(tntPlayer -> {
            final Player player = Bukkit.getPlayer(tntPlayer.getUuid());
            player.sendMessage(Messages.GAME_ENDED.replace("{winner}", Bukkit.getPlayer(Utils.getWinner(arena).getUuid()).getDisplayName()));
        });
    }

    // a simple method to reset all arena timers
    private void resetTimers(final Arena arena) {
        arena.resetLobbyCountdownTimer();
        arena.resetGameTimer();
        arena.resetExplosionCountdownTimer();
    }

    // if the total players divided by 2 bigger is than or equals to the current amount of taggers, add another tagger
    private void selectTaggers(final Arena arena) {
        int taggers = 1;
        final List<TNTPlayer> tntPlayers = new ArrayList<>(arena.getArenaMembers());
        final int size = tntPlayers.size();

        if (tntPlayers.stream().filter(tntPlayer -> !tntPlayer.isSpectator()).count() == 2) {
            final TNTPlayer newTagger = tntPlayers.get(0);
            newTagger.setTagger(true);
            final Player newPlayer = Bukkit.getPlayer(newTagger.getUuid());
            setTagger(newPlayer);
        }

        while (taggers <= (size / 2)) {
            final int random = new Random().nextInt(size);
            final TNTPlayer newTagger = tntPlayers.get(random);
            if (!newTagger.isSpectator() && !newTagger.isTagger()) {
                newTagger.setTagger(true);
                final Player player = Bukkit.getPlayer(newTagger.getUuid());
                setTagger(player);
            }
            taggers++;
        }
    }

    // handles the explosion timer, if it runs out, play the explosion effect
    private void explosionTimer(final Arena arena) {
        if (arena.getArenaMembers().size() > 1) {
            if (arena.getExplosionCountdown() > 0) {
                arena.decrementExplosionCountdown();
            } else {
                arena.resetExplosionCountdownTimer();
                playExplosion(arena);
                selectTaggers(arena);
            }
        } else {
            setGameState(GameState.END, arena);
        }
    }

    // sends out the message to everyone and explodes the taggers
    private void playExplosion(final Arena arena) {
        arena.getArenaMembers().forEach(tntPlayer -> Bukkit.getPlayer(tntPlayer.getUuid()).sendMessage(Utils.colorize("&cBOOOOOM, the taggers have been exploded!")));
        arena.getArenaMembers().stream().filter(tntTagger -> tntTagger.isTagger() && !tntTagger.isSpectator()).forEach(tntTagger -> {
            final Player tagger = Bukkit.getPlayer(tntTagger.getUuid());
            tagger.getWorld().createExplosion(tagger.getLocation(), 0, false);
            tntTagger.setSpectator(true);
            tagger.setGameMode(GameMode.SPECTATOR);
        });
        if (arena.getArenaMembers().stream().filter(tntPlayer -> !tntPlayer.isSpectator()).count() == 1) {
            setGameState(GameState.END, arena);
        }
    }

    // updating the effects from the tagger and the non-taggers
    private void updateEffects(final Arena arena) {
        arena.getArenaMembers().stream().filter(TNTPlayer::isTagger).forEach(tagger ->
                Bukkit.getPlayer(tagger.getUuid()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2)));

        // checking if the players are not taggers and not spectators
        arena.getArenaMembers().stream().filter(tntPlayer -> !tntPlayer.isTagger() && !tntPlayer.isSpectator()).forEach(tagger ->
                Bukkit.getPlayer(tagger.getUuid()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1)));
    }

    // basic method to set the new tagger up
    private void setTagger(final Player player) {
        player.setPlayerListName(Utils.colorize("&c" + player.getDisplayName()));
        player.sendMessage(Messages.TAGGER);
        player.getInventory().addItem(new ItemStack(Material.TNT));
        player.getInventory().setHelmet(new ItemStack(Material.TNT));
    }
}

