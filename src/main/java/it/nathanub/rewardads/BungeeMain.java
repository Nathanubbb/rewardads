package it.nathanub.rewardads;

import it.nathanub.rewardads.Bungee.Logic.Requests;
import it.nathanub.rewardads.Bungee.Logic.Commands.Commands;
import it.nathanub.rewardads.Bungee.Logic.Events;
import it.nathanub.rewardads.Bungee.Tools.Logs.Error;
import it.nathanub.rewardads.Bungee.Tools.Server.Server;
import it.nathanub.rewardads.Bungee.Tools.Version.Version;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class BungeeMain extends Plugin {
    private static BungeeMain instance;
    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;

        try {
            saveDefaultConfig();
            final Server server = new Server(this);
            final Version version = new Version(this);

            if(server.isValid()) {
                ProxyServer.getInstance().getPluginManager().registerCommand(this, new Commands(this));
                ProxyServer.getInstance().getPluginManager().registerListener(this, new Events(this));
                ProxyServer.getInstance().registerChannel("rewardads");
                createUserData();

                getLogger().info("Library RewardADs (BungeeCord) enabled!");
                getLogger().info("Welcome back " + server.getName() + " to RewardADs!");

                ProxyServer.getInstance().getScheduler().schedule(this, new Requests(this), 0, 1, java.util.concurrent.TimeUnit.SECONDS);

                version.checkForUpdate();
            } else {
                getLogger().severe("The code isn't valid in config. Insert a valid code by creating a server on https://rewardads.it/console.");
            }
        } catch(Exception e) {
            Error.send(getConfig().getString("code"), e);
        }
    }

    public Configuration getConfig() {
        return config;
    }

    private void createUserData() throws IOException {
        File userFile = new File(getDataFolder(), "userdata.yml");

        if(!userFile.exists()) {
            getDataFolder().mkdirs();
            userFile.createNewFile();
        }
    }

    private void saveDefaultConfig() throws IOException {
        File configFile = new File(getDataFolder(), "configuration.yml");
        if(!configFile.exists()) {
            getDataFolder().mkdirs();

            InputStream inputStream = getResourceAsStream("configuration.yml");
            FileOutputStream outputStream = new FileOutputStream(configFile);

            if(inputStream != null) {
                byte[] buffer = new byte[1024];
                int length;

                while((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } else {
                getLogger().warning("Default configuration.yml not found in the plugin JAR.");
            }
        }

        loadConfig();
    }


    private void loadConfig() throws IOException {
        File configFile = new File(getDataFolder(), "configuration.yml");

        if(!configFile.exists()) {
            saveDefaultConfig();
        }

        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    @Override
    public void onDisable() {
        getLogger().info("Library RewardADs (BungeeCord) disabled!");
    }

    public static BungeeMain getInstance() {
        return instance;
    }
}
