package com.github.gavvydizzle.nightvision;

import com.github.mittenmc.serverutils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager implements Listener {

    private final NightVision instance;
    private final HashMap<UUID, Boolean> map;
    private final PotionEffect infiniteNightVision;

    // Messages
    private String onMessage, offMessage;

    public PlayerManager(NightVision instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);

        map = new HashMap<>();
        infiniteNightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 0);

        reload();
    }

    public void reload() {
        FileConfiguration config = instance.getConfig();
        config.options().copyDefaults(true);
        config.addDefault("onMessage", "&b[NightVision] &aYou will now have permanent night vision");
        config.addDefault("offMessage", "&b[NightVision] &eTurned off permanent night vision");
        instance.saveConfig();

        onMessage = Colors.conv(config.getString("onMessage"));
        offMessage = Colors.conv(config.getString("offMessage"));
    }

    public void toggleNightVision(Player player) {
        if (DataConfig.toggleNightVision(player)) {
            player.sendMessage(onMessage);
            giveInfiniteNightVision(player);
        }
        else {
            player.sendMessage(offMessage);
            removeInfiniteNightVision(player);
        }
    }

    private void giveInfiniteNightVision(Player player) {
        player.addPotionEffect(infiniteNightVision);
    }

    private void removeInfiniteNightVision(Player player) {
        player.removePotionEffect(infiniteNightVision.getType());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        if (DataConfig.isNightVisionActive(e.getPlayer())) {
            giveInfiniteNightVision(e.getPlayer());
            map.put(e.getPlayer().getUniqueId(), true);
        }
        else {
            map.put(e.getPlayer().getUniqueId(), false);
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        map.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    private void respawnEvent(PlayerRespawnEvent e) {
        if (map.containsKey(e.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().runTask(instance, () -> giveInfiniteNightVision(e.getPlayer()));
        }
    }

    @EventHandler
    private void respawnEvent(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.MILK_BUCKET && map.containsKey(e.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().runTask(instance, () -> giveInfiniteNightVision(e.getPlayer()));
        }
    }

}
