package me.wolf.wtnttag.listeners;

import me.wolf.wtnttag.TNTTagPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChange implements Listener {

    private final TNTTagPlugin plugin;

    public FoodChange(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (plugin.getTntPlayers().containsKey(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

}
