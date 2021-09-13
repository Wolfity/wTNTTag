package me.wolf.wtnttag.listeners;

import me.wolf.wtnttag.TNTTagPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    private final TNTTagPlugin plugin;

    public BlockPlace(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getTntPlayers().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}

