package it.nathanub.rewardads.Spigot.Tools.Accounts.Temporary;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.nathanub.rewardads.Spigot.Tools.Api.Api;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Temporary {
    private final Gson gson;

    public Temporary() {
        this.gson = new Gson();
    }

    public String getAccount(String temp) throws ExecutionException, InterruptedException {
        Future<String> future = Api.handle("getaccountbytemp/" + temp);
        String response = future.get();
        String account = null;

        if(response != null) {
            JsonElement jsonElement = this.gson.fromJson(response, JsonElement.class);

            if(jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                for(JsonElement element : jsonArray) {
                    JsonObject obj = element.getAsJsonObject();

                    if(obj.has("temporary") && obj.get("temporary").getAsString().equals(temp)) {
                        account = obj.get("account").getAsString();
                        break;
                    }
                }
            } else if(jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if(jsonObject.has("temporary") && jsonObject.get("temporary").getAsString().equals(temp)) {
                    account = jsonObject.get("account").getAsString();
                }
            }
        }

        return account != null ? account : "";
    }
}
