package de.pandarmy.pandaServer.offer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.pandarmy.pandaServer.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public <T> void createJSON(T data, String path, File folder) {
        File file = new File(folder, path);

        if (!file.exists()) {
            try {
                file.createNewFile();
                Main.getInstance().getLogger().warning("New File has been created: " + path);
                saveJSON(data, file);
            } catch (IOException e) {
                e.printStackTrace();
                Main.getInstance().getLogger().severe(e.toString());
            }
        }
        Main.getInstance().getConfig().set("JsonOfferCounter", Main.getInstance().getConfig().getInt("JsonOfferCounter") + 1);
        Main.getInstance().saveConfig();
        Main.getInstance().reloadConfig();
    }

    public <T> void saveJSON(T data, File file) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T getJSON(File file, Class<T> clazz) {
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> getAllEntries(File folder, Class<T> clazz) {
        List<T> entries = new ArrayList<>();

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null) return entries;

        for (File file : files) {
            T data = getJSON(file, clazz);
            if (data != null) {
                entries.add(data);
            }
        }

        return entries;
    }

    public void removeFile(File folder, String path){
        File file = new File(folder, path);

        if (file.exists()){
            file.delete();
        }else{
            Main.getInstance().getLogger().warning("File not found: " + path);
        }
    }
}