package it.nathanub.rewardads.Spigot.Tools.Accounts.Temporary;

import org.bukkit.entity.Player;
import java.util.HashMap;

public class Cache {
    private static final HashMap<Player, String> playerMap = new HashMap<>();

    public static void addPlayer(Player player, String temp) {
        playerMap.put(player, temp);
    }

    public static String getTemp(Player player) {
        return playerMap.get(player);
    }

    public static void removePlayer(Player player) {
        playerMap.remove(player);
    }
}
