package me.wolf.wtnttag.listeners;

import me.wolf.wtnttag.TNTTagPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {

    private final TNTTagPlugin plugin;

    public InventoryListener(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(final PlayerDropItemEvent event) {
        if (plugin.getTntPlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    // disabling clicking items in the inventory

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            if (plugin.getTntPlayers().containsKey(event.getWhoClicked().getUniqueId())) {
                final List<ItemStack> items = new ArrayList<>();
                items.add(event.getCurrentItem());
                items.add(event.getCursor());
                items.add((event.getClick() == ClickType.NUMBER_KEY) ?
                        event.getWhoClicked().getInventory().getItem(event.getHotbarButton()) : event.getCurrentItem());
                for (ItemStack item : items) {
                    if (item != null)
                        event.setCancelled(true);
                }
            }
        }
    }

}
