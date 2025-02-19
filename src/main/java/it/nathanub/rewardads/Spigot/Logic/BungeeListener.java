package it.nathanub.rewardads.Spigot.Logic;

import com.google.zxing.WriterException;
import it.nathanub.rewardads.Spigot.Tools.Maps.ImageCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import it.nathanub.rewardads.Spigot.Tools.Logs.Error;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BungeeListener implements PluginMessageListener {
    private final Plugin plugin;

    public BungeeListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player p, byte @NotNull [] message) {
        System.out.println(channel);
        if(!channel.equalsIgnoreCase("rewardads")) {
            return;
        }

        String received = new String(message, StandardCharsets.UTF_8);
        System.out.println(received);
        String[] parts = received.split("_", 4);
        System.out.println(parts[0]);
        if(parts[0].equals("giveqr")) {
            String playerName = parts[1];
            String temp = parts[2];
            String code = parts[3];

            Player player = Bukkit.getPlayer(playerName);
            ImageCreator imageCreator = new ImageCreator(temp);
            try {
                imageCreator.generate(plugin, player);
            } catch (IOException | WriterException e) {
                Error.send(code, e);
            }
        }
    }
}
