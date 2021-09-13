package me.wolf.wtnttag.scoreboard;

import me.wolf.wtnttag.TNTTagPlugin;
import me.wolf.wtnttag.arena.Arena;
import me.wolf.wtnttag.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

@SuppressWarnings("ConstantConditions")
public class Scoreboards {

    private final TNTTagPlugin plugin;

    public Scoreboards(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }

    public void lobbyScoreboard(final Player player, final Arena arena) {
        final int maxPlayers = arena.getArenaConfig().getInt("max-players");
        final String name = arena.getName();
        final int currentPlayers = arena.getArenaMembers().size();

        final ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        final Objective objective = scoreboard.registerNewObjective("tnt", "tnt");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Utils.colorize("&c&lTNT Tag Waiting Room"));

        final Team players = scoreboard.registerNewTeam("players");
        players.addEntry(Utils.colorize("&bPlayers: "));
        players.setPrefix("");
        players.setSuffix(Utils.colorize("&b" + currentPlayers + "&3/&b" + maxPlayers));
        objective.getScore(Utils.colorize("&bPlayers: ")).setScore(1);

        final Team empty1 = scoreboard.registerNewTeam("empty1");
        empty1.addEntry(" ");
        empty1.setPrefix("");
        empty1.setSuffix("");
        objective.getScore(" ").setScore(2);

        final Team map = scoreboard.registerNewTeam("map");
        map.addEntry(Utils.colorize("&bMap: &2"));
        map.setPrefix("");
        map.setSuffix(Utils.colorize(name));
        objective.getScore(Utils.colorize("&bMap: &2")).setScore(3);

        final Team empty2 = scoreboard.registerNewTeam("empty2");
        empty2.addEntry("  ");
        empty2.setPrefix("");
        empty2.setSuffix("");
        objective.getScore("  ").setScore(4);


        player.setScoreboard(scoreboard);
    }

    public void gameScoreboard(final Player player, final Arena arena) {
        final int maxPlayers = arena.getArenaConfig().getInt("max-players");
        final String name = arena.getName();
        final int currentPlayers = (int) arena.getArenaMembers().stream().filter(tntPlayer -> !tntPlayer.isSpectator()).count();


        final ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        final Objective objective = scoreboard.registerNewObjective("tntingame", "tntingame");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Utils.colorize("&c&lTNT Tag"));

        final Team players = scoreboard.registerNewTeam("players");
        players.addEntry(Utils.colorize("&bPlayers: "));
        players.setPrefix("");
        players.setSuffix(Utils.colorize("&b" + currentPlayers + "&3/&b" + maxPlayers));
        objective.getScore(Utils.colorize("&bPlayers: ")).setScore(1);

        final Team empty1 = scoreboard.registerNewTeam("empty1");
        empty1.addEntry(" ");
        empty1.setPrefix("");
        empty1.setSuffix("");
        objective.getScore(" ").setScore(2);

        final Team time = scoreboard.registerNewTeam("time");
        time.addEntry(Utils.colorize("&bTime Left: "));
        time.setPrefix("");
        time.setSuffix(Utils.colorize("&3" + arena.getGameTimer()));
        objective.getScore(Utils.colorize("&bTime Left: ")).setScore(3);

        final Team empty2 = scoreboard.registerNewTeam("empty2");
        empty2.addEntry("  ");
        empty2.setPrefix("");
        empty2.setSuffix("");
        objective.getScore("  ").setScore(4);


        final Team map = scoreboard.registerNewTeam("map");
        map.addEntry(Utils.colorize("&bMap: &2"));
        map.setPrefix("");
        map.setSuffix(Utils.colorize(name));
        objective.getScore(Utils.colorize("&bMap: &2")).setScore(5);

        final Team empty4 = scoreboard.registerNewTeam("empty4");
        empty4.addEntry("    ");
        empty4.setPrefix("");
        empty4.setSuffix("");
        objective.getScore("    ").setScore(6);

        final Team explosion = scoreboard.registerNewTeam("explosion");
        explosion.addEntry(Utils.colorize("&cExplosion: &e"));
        explosion.setPrefix("");
        explosion.setSuffix(String.valueOf(arena.getExplosionCountdown()));
        objective.getScore(Utils.colorize("&cExplosion: &e")).setScore(6);

        final Team empty5 = scoreboard.registerNewTeam("empty5");
        empty5.addEntry("     ");
        empty5.setPrefix("");
        empty5.setSuffix("");
        objective.getScore("     ").setScore(7);
        player.setScoreboard(scoreboard);

    }

}
