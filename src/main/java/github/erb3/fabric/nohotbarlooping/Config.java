package github.erb3.fabric.nohotbarlooping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Config {

    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "NoHotbarLooping.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(NoHotbarLooping.shouldLoopHotbar, writer);
        } catch (IOException e) {
            NoHotbarLooping.logger.error("Could not save No Hotbar Looping configuration!", e);
        }
    }

    public void loadConfig() {
        try (Reader reader = new FileReader(configFile)) {
            NoHotbarLooping.shouldLoopHotbar = gson.fromJson(reader, Boolean.class);
        } catch (Exception e) {
            if (configFile.exists()) {
                NoHotbarLooping.logger.error("No Hotbar Looping configuration file exists, but could not load.", e);
            } else {
                NoHotbarLooping.logger.warn("No Hotbar Looping configuration file does not exist!");
            }
        }
    }

    public void toggle() {
        NoHotbarLooping.shouldLoopHotbar = !NoHotbarLooping.shouldLoopHotbar;
        this.saveConfig();
    }

}
