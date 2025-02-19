package it.nathanub.rewardads.Spigot.Tools.Maps;

import it.nathanub.rewardads.Spigot.Tools.Version.Version;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class MapCreator {
    public static ItemStack generateMap(Plugin plugin, BufferedImage image, Player p, String data) {
        Version version = new Version(plugin);
        ItemStack itemStack;
        MapMeta mapMeta;

        if(version.isServerHigher()) {
            itemStack = new ItemStack(Material.FILLED_MAP);
            mapMeta = (MapMeta) itemStack.getItemMeta();
            MapView mapView = Bukkit.createMap(p.getWorld());
            mapView.setScale(MapView.Scale.CLOSEST);
            mapView.setUnlimitedTracking(true);

            mapView.getRenderers().clear();
            mapView.addRenderer(new MapRenderer() {
                @Override
                public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                    mapCanvas.drawImage(0, 0, image);
                }
            });

            if (mapMeta != null) {
                mapMeta.setMapView(mapView);
                itemStack.setItemMeta(mapMeta);
            }
        } else {
            itemStack = new ItemStack(Material.MAP);
            mapMeta = (MapMeta) itemStack.getItemMeta();
            MapView mapView = Bukkit.createMap(p.getWorld());
            mapView.setScale(MapView.Scale.CLOSEST);

            mapView.getRenderers().clear();
            mapView.addRenderer(new MapRenderer() {
                @Override
                public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                    mapCanvas.drawImage(0, 0, image);
                }
            });

            if(mapMeta != null)
                itemStack.setItemMeta(mapMeta);
        }

        ItemMeta meta = itemStack.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(data);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
