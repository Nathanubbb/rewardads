package it.nathanub.rewardADsInter;

import it.nathanub.rewardads.Logic.Buy.OnBuy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Events implements Listener {
    private final Plugin plugin;

    public Events(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBuy(OnBuy event) {
        Player player = event.getPlayer();
        String nameReward = event.getNameReward();
        String idReward = event.getIdReward();
        String costReward = event.getCostReward();

        String commandReward = plugin.getConfig().getString("rewards." + idReward + ".command");

        if(commandReward != null) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), commandReward
                    .replace("%player%", player.getName())
                    .replace("%name%", nameReward)
                    .replace("%cost%", costReward)
                    .replace("%id%", idReward));
        } else {
            player.sendMessage("No action found for: " + idReward);
        }
    }
}
