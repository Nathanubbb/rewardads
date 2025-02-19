package it.nathanub.rewardads.Bungee.Tools.Accounts;

import it.nathanub.rewardads.Bungee.Tools.Accounts.Temporary.Cache;
import it.nathanub.rewardads.Bungee.Tools.Api.Api;
import it.nathanub.rewardads.Bungee.Tools.User.User;
import it.nathanub.rewardads.BungeeMain;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Link {
    private final BungeeMain plugin;
    private final FileConfiguration userConfig;
    private final File userFile;

    public Link(BungeeMain plugin) throws IOException {
        File userFile = new File(plugin.getDataFolder(), "userdata.yml");
        if(!userFile.exists()) {
            if(userFile.createNewFile()) {
                plugin.getLogger().info("Created new userdata.yml file.");
            } else {
                plugin.getLogger().warning("Failed to create userdata.yml file.");
            }
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(userFile);
        if(!yamlConfiguration.isConfigurationSection("users")) {
            yamlConfiguration.createSection("users");
            yamlConfiguration.save(userFile);
        }

        this.plugin = plugin;
        this.userConfig = yamlConfiguration;
        this.userFile = userFile;
    }

    public String handleLink(ProxiedPlayer player, String account) throws IOException, ParseException, ExecutionException, InterruptedException {
        UUID uuid = player.getUniqueId();
        User user = new User(plugin);
        String message;

        // Delete temporary account from cache
        Api.handle("deltemp/" + account);
        //removeMap(player, Cache.getTemp(player));
        Cache.removePlayer(player);

        if(!this.userConfig.contains("users." + uuid)) {
            this.userConfig.set("users." + uuid, account);
            saveUserData();

            message = "Signed in as " + user.getName(account) + "!";
        } else {
            message = "You're already linked!";
        }

        return message;
    }

    public String handleUnlink(ProxiedPlayer player) throws IOException {
        UUID uuid = player.getUniqueId();

        if(this.userConfig.contains("users." + uuid)) {
            this.userConfig.set("users." + uuid, null);
            saveUserData();

            return "Successfully unlinked.";
        }

        return "No account linked.";
    }

    public boolean isLinked(Object value) {
        if(this.userConfig == null) {
            throw new IllegalStateException("User configuration is not loaded.");
        }
        if(!this.userConfig.isConfigurationSection("users")) {
            throw new IllegalStateException("'users' section is missing in the configuration.");
        }

        String targetValue;
        if (value instanceof Player) {
            targetValue = ((Player) value).getUniqueId().toString();
        } else if (value instanceof String) {
            targetValue = (String) value;
        } else {
            throw new IllegalArgumentException("Invalid type passed to isLinked method");
        }

        return this.userConfig.getKeys(false).stream()
                .anyMatch(key -> Objects.equals(this.userConfig.getString("users." + key), targetValue));
    }

    private void saveUserData() throws IOException {
        this.userConfig.save(this.userFile);
    }
}
