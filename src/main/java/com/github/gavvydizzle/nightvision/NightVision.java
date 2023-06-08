package com.github.gavvydizzle.nightvision;

import com.github.gavvydizzle.nightvision.command.AdminCommands;
import com.github.gavvydizzle.nightvision.command.PlayerCommands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class NightVision extends JavaPlugin {

    private static NightVision instance;

    @Override
    public void onEnable() {
        instance = this;
        PlayerManager playerManager = new PlayerManager(this);

        Objects.requireNonNull(getServer().getPluginCommand("nightvision")).setExecutor(new PlayerCommands(playerManager));
        Objects.requireNonNull(getServer().getPluginCommand("nightvisionadmin")).setExecutor(new AdminCommands(this, playerManager));
    }

    @Override
    public void onDisable() {
        try {
            DataConfig.save();
            Bukkit.getLogger().info("Successfully saved player data");
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to save player data");
            e.printStackTrace();
        }
    }

    public static NightVision getInstance() {
        return instance;
    }
}
