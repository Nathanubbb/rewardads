package it.nathanub.rewardads.Spigot.Tools.Accounts;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import it.nathanub.rewardads.Spigot.Tools.Accounts.Temporary.Cache;
import it.nathanub.rewardads.Spigot.Tools.User.User;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.json.simple.parser.ParseException;

public class Link {
    private final Plugin plugin;

    private final FileConfiguration userConfig;

    private final File userFile;

    public Link(Plugin plugin) throws IOException {
        File userFile = new File(plugin.getDataFolder(), "userdata.yml");
        if(!userFile.exists())
            if(userFile.createNewFile())
                plugin.getLogger().info("Created new userdata.yml file.");

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(userFile);
        if(!yamlConfiguration.isConfigurationSection("users")) {
            yamlConfiguration.createSection("users");
            yamlConfiguration.save(userFile);
        }

        this.plugin = plugin;
        this.userConfig = yamlConfiguration;
        this.userFile = userFile;
    }

    public String handleLink(Player player, String account) throws IOException, ParseException, ExecutionException, InterruptedException {
        UUID uuid = player.getUniqueId();
        User user = new User(plugin);
        String message;

        it.nathanub.rewardads.Spigot.Tools.Api.Api.handle("deltemp/" + account);
        removeMap(player, Cache.getTemp(player));
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

    public String handleUnlink(Player player) throws IOException {
        UUID uuid = player.getUniqueId();

        if(this.userConfig.contains("users." + uuid)) {
            this.userConfig.set("users." + uuid, null);
            saveUserData();

            return "Successfully unlinked.";
        }

        return "No account linked.";
    }

    public boolean isLinked(String value) {
        if(this.userConfig == null) {
            throw new IllegalStateException("User configuration is not loaded.");
        }
        if(value == null || value.isEmpty()) {
            throw new IllegalStateException("Invalid value link provided: " + value);
        }
        if(!this.userConfig.isConfigurationSection("users")) {
            throw new IllegalStateException("'users' section is missing in the configuration.");
        }

        for(String key : Objects.requireNonNull(this.userConfig.getConfigurationSection("users")).getKeys(false)) {
            String linkedValue = this.userConfig.getString("users." + key);
            if(value.equals(linkedValue))
                return true;
        }

        return false;
    }

    public boolean isLinked(Player player) {
        if(this.userConfig == null) {
            throw new IllegalStateException("User configuration is not loaded.");
        }
        if(!this.userConfig.isConfigurationSection("users")) {
            throw new IllegalStateException("'users' section is missing in the configuration.");
        }

        String playerUuid = player.getUniqueId().toString();
        return this.userConfig.contains("users." + playerUuid);
    }

    private void saveUserData() throws IOException {
        this.userConfig.save(this.userFile);
    }

    public static void removeMap(Player player, String targetName) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item != null && item.getType() == Material.MAP) {
                ItemMeta meta = item.getItemMeta();

                if(meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(targetName)) {
                    player.getInventory().remove(item);
                }
            }
        }
    }
}
