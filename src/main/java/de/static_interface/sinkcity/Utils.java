package de.static_interface.sinkcity;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

    public static String format(final String formatString, Object... arguments) {
        String newString = formatString;
        for (int x = 0; x < arguments.length; x++) {
            if (newString.contains("{" + x + "}")) {
                newString = newString.replace("{" + x + "}", arguments[x].toString());
            }
        }

        newString = ChatColor.translateAlternateColorCodes('&', newString);
        return newString;
    }

    public static Player getPlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }

        return null;
    }

    public static String getDisplayNameById(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return null;
        return player.getDisplayName();
    }
}
