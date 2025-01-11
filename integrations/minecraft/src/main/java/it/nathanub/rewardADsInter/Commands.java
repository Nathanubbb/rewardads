package it.nathanub.rewardADsInter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Commands implements CommandExecutor {
    private final Plugin plugin;

    public Commands(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("rewardadsinterface")) {
            if(args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    plugin.reloadConfig();
                    player.sendMessage("§bRewardADsInteface§a reloaded successfully!");
                } else {
                    plugin.reloadConfig();
                    sender.sendMessage("§bRewardADsInteface§a reloaded successfully!");
                }
            } else {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage("This is a premade interface for §6RewardADs");

                    return true;
                } else {
                    sender.sendMessage("This is a premade interface for §6RewardADs");
                }
            }
        }

        return false;
    }
}