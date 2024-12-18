package it.nathanub.rewardADsInter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    PluginManager pluginManager = Bukkit.getPluginManager();
    Plugin RewardADs = pluginManager.getPlugin("RewardADs");

    {
        assert RewardADs != null;
    }

    public static void main(String[] args) {}

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this), this);
        Objects.requireNonNull(this.getCommand("rewardadsinterface")).setExecutor(new Commands(this));
        saveDefaultConfig();

        getLogger().info("RewardADs Interface enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RewardADs Interface disabled!");
    }
}
