package com.github.gavvydizzle.nightvision.command;

import com.github.gavvydizzle.nightvision.NightVision;
import com.github.gavvydizzle.nightvision.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminCommands implements TabExecutor {

    private final ArrayList<String> args2 = new ArrayList<>(Collections.singletonList("reload"));
    private final NightVision instance;
    private final PlayerManager playerManager;

    public AdminCommands(NightVision instance, PlayerManager playerManager) {
        this.instance = instance;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You must supply a sub-command");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            try {
                instance.reloadConfig();
                playerManager.reload();
                sender.sendMessage(ChatColor.GREEN + "[NightVision] Reloaded");
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "[NightVision] Failed to reload. Check the console for errors");
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], args2, list);
        }
        return list;
    }
}