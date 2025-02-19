package it.nathanub.rewardads.Bungee.Logic.Commands;

import it.nathanub.rewardads.Bungee.Tools.Accounts.Link;
import it.nathanub.rewardads.Bungee.Tools.Accounts.Temporary.Cache;
import it.nathanub.rewardads.Bungee.Tools.Accounts.Verify;
import it.nathanub.rewardads.Bungee.Tools.Api.Api;
import it.nathanub.rewardads.Bungee.Tools.Logs.Error;
import it.nathanub.rewardads.Bungee.Tools.Server.Server;
import it.nathanub.rewardads.Bungee.Tools.Version.Version;
import it.nathanub.rewardads.BungeeMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class Commands extends Command {
    private final BungeeMain plugin;
    private final String code;

    public Commands(BungeeMain plugin) {
        super("rewardads");
        this.plugin = plugin;
        this.code = plugin.getConfig().getString("code");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            String prefix = "§7[§6R§7] §f";

            if(args.length == 0) {
                String version = plugin.getDescription().getVersion();
                Server server = new Server(plugin);

                if(sender instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) sender;

                    if(server.isValid()) {
                        player.sendMessage(new TextComponent(prefix + "§a" + server.getName() + " §fis using §6RewardADs §7v" + version + "§f!"));
                    } else {
                        player.sendMessage(new TextComponent(prefix + "§cRewardADs is not configured, please contact an administrator."));
                    }
                } else {
                    if(server.isValid()) {
                        sender.sendMessage(new TextComponent(("§fYour server is using §6RewardADs §7v" + version + "§f!")));
                    } else {
                        sender.sendMessage(new TextComponent("§cRewardADs is not configured, do it!"));
                    }
                }
            } else if (args[0].equalsIgnoreCase("version")) {
                Version version = new Version(plugin);
                sender.sendMessage(new TextComponent(prefix + "§7v" + version.getPlugin()));
            } else if (args[0].equalsIgnoreCase("link")) {
                if(!(sender instanceof ProxiedPlayer)) {
                    sender.sendMessage(new TextComponent(prefix + "§cThis command is only for players."));
                    return;
                }

                ProxiedPlayer player = (ProxiedPlayer) sender;
                String temp = randomTemp();

                Cache.removePlayer(player);
                Cache.addPlayer(player, temp);
                Api.handle("addtemp/" + temp);

                TextComponent textComponent = new TextComponent(prefix + "If you can't scan the QR, click here to copy your temporary code.");
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, temp));

                player.sendMessage(new TextComponent(prefix + "§aLink your account instantly! Scan the QR code in the RewardADs app on your phone!"));
                player.sendMessage(textComponent);

                String message = "giveqr_" + player.getName() + "_" + temp + "_" + code;
                System.out.println(message);
                System.out.println(player.getServer().getInfo().getName());


                if (ProxyServer.getInstance().getServerInfo(player.getServer().getInfo().getName()) != null) {
                    System.out.println("Sending plugin message to: " + player.getServer().getInfo().getName());
                    ProxyServer.getInstance().getServerInfo(player.getServer().getInfo().getName())
                            .sendData("rewardads", message.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Message sent successfully.");
                } else {
                    System.out.println("Failed to find server: " + player.getServer().getInfo().getName());
                }

            } else if (args[0].equalsIgnoreCase("unlink")) {
                if (!(sender instanceof ProxiedPlayer)) {
                    sender.sendMessage(new TextComponent(prefix + "§cThis command is only for players."));
                    return;
                }

                ProxiedPlayer player = (ProxiedPlayer) sender;
                Link link = new Link(plugin);
                player.sendMessage(new TextComponent(prefix + link.handleUnlink(player)));
            } else if (args[0].equalsIgnoreCase("confirm")) {
                if (args.length < 3) {
                    sender.sendMessage(new TextComponent(prefix + "§cInsert token and platformId!"));
                    return;
                }

                String token = args[1];
                String platform_id = args[2];

                if (!(sender instanceof ProxiedPlayer)) {
                    sender.sendMessage(new TextComponent(prefix + "§cThis command is only for players."));
                    return;
                }

                ProxiedPlayer player = (ProxiedPlayer) sender;
                Verify verify = new Verify(plugin);
                player.sendMessage(new TextComponent(prefix + verify.verifyPlatform(player, token, platform_id)));
                plugin.getLogger().info(player.getName() + " Verified platform!");
            } else {
                sender.sendMessage(new TextComponent(prefix + "§cUnknown command."));
            }
        } catch (Exception e) {
            sender.sendMessage(new TextComponent("§cOops, an error occurred. Please report it to the server's administrator."));
            Error.send(this.code, e);
        }
    }

    private static String randomTemp() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }
}
