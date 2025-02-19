package it.nathanub.rewardads.Bungee.Tools.Accounts.Temporary;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class Cache {
    private static final HashMap<ProxiedPlayer, String> playerMap = new HashMap<>();

    public static void addPlayer(ProxiedPlayer player, String temp) {
        playerMap.put(player, temp);
    }

    public static String getTemp(ProxiedPlayer player) {
        return playerMap.get(player);
    }

    public static void removePlayer(ProxiedPlayer player) {
        playerMap.remove(player);
    }
}
