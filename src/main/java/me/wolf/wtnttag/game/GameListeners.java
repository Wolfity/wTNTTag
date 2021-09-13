package me.wolf.wtnttag.game;

import me.wolf.wtnttag.TNTTagPlugin;
import me.wolf.wtnttag.constants.Messages;
import me.wolf.wtnttag.player.TNTPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class GameListeners implements Listener {

    private final TNTTagPlugin plugin;

    public GameListeners(final TNTTagPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTag(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player tagger = (Player) event.getDamager();
            final Player tagged = (Player) event.getEntity();
            final TNTPlayer tntTagger = plugin.getTntPlayers().get(tagger.getUniqueId());
            final TNTPlayer tntTagged = plugin.getTntPlayers().get(tagged.getUniqueId());

            // if the tagger is a tagger, set the tagged to the new tagger, and remove the tagger from the current tagger
            if (tntTagger.isTagger()) {
                setToNormal(tagger);
                setToTagger(tagged);
            }
        }
    }

    private void setToTagger(final Player tagged) { // change a regular player to a tagger
        final TNTPlayer tntTagged = plugin.getTntPlayers().get(tagged.getUniqueId());
        tntTagged.setTagger(true);
        tagged.sendMessage(Messages.GOT_TAGGED);
        tagged.setPlayerListName("&e" + tagged.getDisplayName());
        tagged.getInventory().addItem(new ItemStack(Material.TNT));
        tagged.getInventory().setHelmet(new ItemStack(Material.TNT));
    }

    private void setToNormal(final Player tagger) { // change the tagger to a regular player
        final TNTPlayer tntTagger = plugin.getTntPlayers().get(tagger.getUniqueId());
        tntTagger.setTagger(false);
        tagger.setPlayerListName(Messages.TAGGED_SOMEONE);
        tagger.setPlayerListName(tagger.getDisplayName());
        tagger.getInventory().setHelmet(null);
        tagger.getInventory().clear();
    }

}
