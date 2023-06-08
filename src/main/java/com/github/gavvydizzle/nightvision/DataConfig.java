package com.github.gavvydizzle.nightvision;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class DataConfig {

    private static File file;
    private static FileConfiguration fileConfiguration;
    private static final String path = "data.";

    static {
        setup();
        save();
    }

    //Finds or generates the config file
    public static void setup() {
        file = new File(NightVision.getInstance().getDataFolder(), "data.yml");
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return fileConfiguration;
    }

    public static void save() {
        try {
            fileConfiguration.save(file);
        }
        catch (IOException e) {
            System.out.println("Could not save file");
        }
    }

    public static void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Retrieves the player's night vision state
     * @param player The player
     * @return If night vision should be active
     */
    public static boolean isNightVisionActive(Player player) {
        return fileConfiguration.getBoolean(path + player.getUniqueId());
    }

    /**
     * Save a player's drop type to the config
     * @param player The player
     * @return  If night vision is not active
     */
    public static boolean toggleNightVision(Player player) {
        boolean current = fileConfiguration.getBoolean(path + player.getUniqueId());
        fileConfiguration.set(path + player.getUniqueId(), !current);
        return !current;
    }

}