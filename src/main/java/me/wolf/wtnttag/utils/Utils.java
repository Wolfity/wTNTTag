package me.wolf.wtnttag.utils;

import me.wolf.wtnttag.arena.Arena;
import me.wolf.wtnttag.player.TNTPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class Utils {

    public static String colorize(final String input) {
        return input == null ? "Null value" : ChatColor.translateAlternateColorCodes('&', input);
    }


    public static String[] colorize(String... messages) {
        String[] colorized = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            colorized[i] = ChatColor.translateAlternateColorCodes('&', messages[i]);
        }
        return colorized;
    }

    public static ItemStack createItem(final Material material, final String name, final int amount) {

        ItemStack is = new ItemStack(material, amount);
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.colorize(name));

        is.setItemMeta(meta);
        return is;
    }

    public static TNTPlayer getWinner(final Arena arena) {
        return arena.getArenaMembers().stream().filter(tntPlayer -> !tntPlayer.isSpectator()).collect(Collectors.toList()).get(0);
    }

    public static boolean calculateChance(final double chance) {
        return Math.random() * 100 < chance;
    }

    private Utils() {
    }

}
