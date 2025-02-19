package it.nathanub.rewardads.Bungee.Logic.Buy;

import it.nathanub.rewardads.Bungee.Tools.Api.Api;
import it.nathanub.rewardads.BungeeMain;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class Buy {
    public void handle(BungeeMain plugin, Map<String, String> event) throws IOException {
        String code = event.get("code");
        String idReward = event.get("id");
        String nameReward = event.get("name");
        String costReward = event.get("cost");
        String userId = event.get("user");

        if(userId == null || idReward == null || nameReward == null || costReward == null || code == null) return;

        UUID uuid = UUID.fromString(userId);
        ProxiedPlayer player = plugin.getProxy().getPlayer(uuid);

        if(player != null && player.isConnected()) {
            new OnBuy(plugin, player, idReward, nameReward, costReward, code);

            update(event, "ok");
        } else {
            update(event, "Player is not online");
        }
    }

    private void update(Map<String, String> event, String status) {
        String idReward = event.get("id");
        String userId = event.get("user");
        String code = event.get("code");
        String quantity = event.get("quantity");

        Api.handle("updatebuy/" + idReward + "/" + userId + "/" + code + "/" + quantity + "/" + status);
    }
}
