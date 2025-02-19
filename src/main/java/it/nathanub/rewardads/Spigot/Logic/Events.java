package it.nathanub.rewardads.Spigot.Logic;

import com.google.zxing.WriterException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class Events implements Listener {
    private final Plugin plugin;
    public String playerName;

    public Events(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        this.playerName = player.getName();
    }

    @EventHandler
    public void onMapInitialize(MapInitializeEvent e) throws WriterException {
        String version = Bukkit.getServer().getVersion();
        MapView mapView = e.getMap();
        ItemStack mapItem;

        Player player = Objects.requireNonNull(mapView.getWorld()).getPlayers().stream().findFirst().orElse(null);
        if(player == null) return;

        if(version.split(" ")[1].compareTo("1.13") >= 0) {
            mapItem = player.getInventory().getItemInMainHand();
        } else {
            mapItem = player.getInventory().getItemInHand();
        }

        if(mapItem.hasItemMeta()) {
            ItemMeta meta = mapItem.getItemMeta();

            if(meta != null) {
                String data = meta.getDisplayName();

                if(!data.isEmpty()) {
                    BufferedImage image = it.nathanub.rewardads.Spigot.Tools.Maps.ImageCreator.generateQRcode(data);
                    mapView.getRenderers().clear();
                    mapView.addRenderer(new MapRenderer() {
                        @Override
                        public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                            mapCanvas.drawImage(0, 0, image);
                        }
                    });
                }
            }
        }
    }
}
