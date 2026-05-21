package de.pandarmy.pandaServer.offer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.pandarmy.pandaServer.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void createJSON(OfferData offerData, String path) {
        File file = new File(Main.offerFolder, path);

        if (!file.exists()) {
            try {
                file.createNewFile();
                Main.getInstance().getLogger().warning("New File has been created: " + path);
                saveJSON(offerData, file);
            } catch (IOException e) {
                e.printStackTrace();
                Main.getInstance().getLogger().severe(e.toString());
            }
        }
        Main.getInstance().getConfig().set("JsonOfferCounter", Main.getInstance().getConfig().getInt("JsonOfferCounter") + 1);
        Main.getInstance().saveConfig();
        Main.getInstance().reloadConfig();
    }

    public void saveJSON(OfferData offerData, File file) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(offerData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OfferData getJSON(File file) {
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, OfferData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OfferData> getAllOffers() {
        List<OfferData> offers = new ArrayList<>();

        File[] files = Main.offerFolder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null) return offers;

        for (File file : files) {
            OfferData data = getJSON(file);
            if (data != null) {
                offers.add(data);
            }
        }

        return offers;
    }
}