package me.wolf.wtnttag.listeners;

import me.wolf.wtnttag.TNTTagPlugin;
import me.wolf.wtnttag.arena.Arena;
import me.wolf.wtnttag.arena.ArenaState;
import me.wolf.wtnttag.player.TNTPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamage implements Listener {

    private final TNTTagPlugin plugin;

    public EntityDamage(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (!plugin.getTntPlayers().containsKey(player.getUniqueId())) return;
            final TNTPlayer tntPlayer = plugin.getTntPlayers().get(player.getUniqueId());
            final Arena arena = plugin.getArenaManager().getArenaByPlayer(tntPlayer);

            if (arena.getArenaState() != ArenaState.INGAME) event.setCancelled(true);
        }
    }


}
