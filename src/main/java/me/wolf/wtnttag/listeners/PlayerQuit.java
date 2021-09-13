package me.wolf.wtnttag.listeners;

import me.wolf.wtnttag.TNTTagPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final TNTTagPlugin plugin;

    public PlayerQuit(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getTntPlayers().containsKey(player.getUniqueId())) {
            plugin.getGameManager().removePlayer(player);
        }
    }

}
