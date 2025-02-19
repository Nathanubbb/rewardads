package it.nathanub.rewardads.Spigot.Tools.Maps;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ImageCreator {
    String data;
    String charset = "UTF-8";

    public ImageCreator(String data) {
        this.data = data;
    }

    public void generate(Plugin plugin, Player player) throws IOException, WriterException {
        BufferedImage image = generateQRcode();
        ItemStack map = MapCreator.generateMap(plugin, image, player, data);

        player.setItemInHand(map);
    }

    public BufferedImage generateQRcode() throws UnsupportedEncodingException, WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, 128, 128);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public static BufferedImage generateQRcode(String data) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), BarcodeFormat.QR_CODE, 128, 128);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
