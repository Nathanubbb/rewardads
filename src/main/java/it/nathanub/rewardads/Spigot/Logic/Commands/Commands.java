package it.nathanub.rewardads.Spigot.Logic.Commands;

import com.google.zxing.WriterException;
import it.nathanub.rewardads.Spigot.Tools.Accounts.Temporary.Cache;
import it.nathanub.rewardads.Spigot.Tools.Accounts.Verify;
import it.nathanub.rewardads.Spigot.Tools.Api.Api;
import it.nathanub.rewardads.Spigot.Tools.Logs.Error;
import it.nathanub.rewardads.Spigot.Tools.Maps.ImageCreator;
import it.nathanub.rewardads.Spigot.Tools.Server.Server;
import it.nathanub.rewardads.Spigot.Tools.Accounts.Link;

import it.nathanub.rewardads.Spigot.Tools.Version.Version;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;

public class Commands implements CommandExecutor {
    private final Plugin plugin;
    private final String code;

    public Commands(Plugin plugin) {
        this.plugin = plugin;
        this.code = plugin.getConfig().getString("code");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        try {
            String prefix = "§7[§6R§7] §f";

            if(command.getName().equalsIgnoreCase("rewardads")) {
                if(args.length == 0) {
                    if(sender instanceof Player) {
                        String version = plugin.getDescription().getVersion();
                        Server server = new Server(plugin);
                        Player player = (Player) sender;

                        if (server.isValid()) {
                            player.sendMessage(prefix + "§a" + server.getName() + " §fis using §6RewardADs §7v" + version + "§f!");
                        } else {
                            player.sendMessage(prefix + "§cRewardADs is not configured, contact an administrator.");
                        }
                    } else {
                        String version = plugin.getDescription().getVersion();
                        Server server = new Server(plugin);

                        if(server.isValid()) {
                            sender.sendMessage("§fYour server is using §6RewardADs §7v" + version + "§f!");
                        } else {
                            sender.sendMessage("§cRewardADs is not configured, do it!");
                        }
                    }

                    return true;
                } else if(args[0].equalsIgnoreCase("reload")) {
                    if(sender instanceof Player) {
                        Player player = (Player) sender;

                        plugin.reloadConfig();
                        player.sendMessage(prefix + "§6RewardADs§a reloaded successfully!");
                    } else {
                        plugin.reloadConfig();
                        sender.sendMessage(prefix + "§6RewardADs§a reloaded successfully!");
                    }
                } else if(args[0].equalsIgnoreCase("version")) {
                    Version version = new Version(plugin);
                    Player player = (Player) sender;

                    player.sendMessage(prefix + "§7v" + version.getPlugin());
                } else if(args[0].equalsIgnoreCase("error")) {
                    throw new IllegalStateException("User configuration is not loaded.");
                }
            } else if(command.getName().equalsIgnoreCase("link")) {
                if(!(sender instanceof Player)) return true;

                Player player = (Player) sender;
                String temp = randomTemp();

                Cache.removePlayer(player);
                ImageCreator imageCreator = new ImageCreator(temp);
                Cache.addPlayer(player, temp);
                Api.handle("addtemp/" + temp);

                TextComponent textComponent = new TextComponent(prefix + "If you can't scan the qr, click here to copy your temporary code.");
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, temp));

                player.sendMessage(prefix + "§aLink your account instantly! Scan the QR code in the RewardADs app on your phone!");
                player.spigot().sendMessage(textComponent);

                imageCreator.generate(plugin, player);
            } else if(command.getName().equalsIgnoreCase("unlink")) {
                Player player = (Player) sender;

                Link link = new Link(plugin);
                player.sendMessage(prefix + link.handleUnlink(player));
                plugin.getLogger().info(player.getName() + " Unliked rewardADs account");
            } else if(command.getName().equalsIgnoreCase("confirm")) {
                if(args.length > 1) {
                    String token = args[0];
                    String platform_id = args[1];
                    Player player = (Player) sender;

                    Verify verify = new Verify(plugin);
                    player.sendMessage(prefix + verify.verifyPlatform(player, token, platform_id));
                    plugin.getLogger().info(player.getName() + " Verified platform!");
                } else {
                    sender.sendMessage(prefix + "§cInsert token and platformId!");
                }
            }

            return false;
        } catch(Exception e) {
            sender.sendMessage("§cOops, an error occurred. Please report it to the server's administrator.");
            Error.send(this.code, e);
            return false;
        }
    }

    private static String randomTemp() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);

        for(int i = 0; i < 10; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }
}
