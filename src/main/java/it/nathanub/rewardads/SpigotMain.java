package it.nathanub.rewardads;

import it.nathanub.rewardads.Spigot.Logic.BungeeListener;
import it.nathanub.rewardads.Spigot.Logic.Requests;
import it.nathanub.rewardads.Spigot.Logic.Commands.Commands;
import it.nathanub.rewardads.Spigot.Logic.Events;
import it.nathanub.rewardads.Spigot.Tools.Logs.Error;
import it.nathanub.rewardads.Spigot.Tools.Server.Server;
import it.nathanub.rewardads.Spigot.Tools.Version.Version;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SpigotMain extends JavaPlugin {
    private final Server server = new Server(this);
    public FileConfiguration userConfig;
    private final Version version = new Version(this);
    private static SpigotMain instance;

    @Override
    public void onEnable() {
        instance = this;

        try {
            saveDefaultConfig();

            if(!getConfig().getBoolean("isBungee") && server.isValid()) {
                Objects.requireNonNull(this.getCommand("rewardads")).setExecutor(new Commands(this));
                Objects.requireNonNull(this.getCommand("unlink")).setExecutor(new Commands(this));
                Objects.requireNonNull(this.getCommand("link")).setExecutor(new Commands(this));
                Objects.requireNonNull(this.getCommand("confirm")).setExecutor(new Commands(this));
                getServer().getPluginManager().registerEvents(new Events(this), this);
                saveDefaultConfig();
                createUserData();getLogger().info("Library RewardADs (Spigot) enabled!");
                getLogger().info("Welcome back " + server.getName() + " to RewardADs!");
                new Requests(this).runTaskTimer(this, 0, 200);
                version.checkForUpdate();
            } else if(!server.isValid()) {
                getLogger().severe("The code isn't valid in config, insert a valid code by creating a server on https://rewardads.it/console, if you haven't already.");
            } else {
                this.getServer().getMessenger().registerIncomingPluginChannel(this, "rewardads", new BungeeListener(this));
                this.getServer().getMessenger().registerOutgoingPluginChannel(this, "rewardads");

                // Debugging: Print both incoming and outgoing channels
                System.out.println("Registered incoming channels: " + String.join(", ", this.getServer().getMessenger().getIncomingChannels()));
                System.out.println("Registered outgoing channels: " + String.join(", ", this.getServer().getMessenger().getOutgoingChannels()));
            }
        } catch(Exception e) {
            Error.send(server.getCode(), e);
        }
    }

    private void createUserData() throws IOException {
        File userFile = new File(getDataFolder(), "userdata.yml");

        if(!userFile.exists()) {
            getDataFolder().mkdirs();
            userFile.createNewFile();
        }

        userConfig = YamlConfiguration.loadConfiguration(userFile);
    }

    @Override
    public void onDisable() {
        getLogger().info("Library RewardADs (Spigot) disabled!");
    }

    public static SpigotMain getInstance() {
        return instance;
    }
}
