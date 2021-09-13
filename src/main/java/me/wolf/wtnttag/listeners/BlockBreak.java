package me.wolf.wtnttag.listeners;

import me.wolf.wtnttag.TNTTagPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private final TNTTagPlugin plugin;

    public BlockBreak(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getTntPlayers().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

}
