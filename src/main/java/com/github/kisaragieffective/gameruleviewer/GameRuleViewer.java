package com.github.kisaragieffective.gameruleviewer;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class GameRuleViewer extends JavaPlugin implements CommandExecutor {

    private static final String COMMAND_NAME = "gamerule-list";
    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand(COMMAND_NAME), "Command gamerule-list is not defined?!").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!command.getName().toLowerCase().equals(COMMAND_NAME)) return false;
        final World world;
        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else if (sender instanceof BlockCommandSender) {
            world = ((BlockCommandSender) sender).getBlock().getWorld();
        } else {
            sender.sendMessage("Not supported");
            return true;
        }
        String[] a = world.getGameRules();
        sender.sendMessage(a.length + " entries");
        for (final String gameRuleKey : a) {
            String value = syntaxHighlight(world.getGameRuleValue(gameRuleKey));
            sender.sendMessage(gameRuleKey + ": " + value);
        }
        return true;
    }

    private String syntaxHighlight(String input) {
        if (input.equals("true")) {
            return ChatColor.GREEN + "true" + ChatColor.RESET;
        } else if (input.equals("false")) {
            return ChatColor.RED + "false" + ChatColor.RESET;
        } else {
            try {
                Integer.parseInt(input);
                return ChatColor.BLUE + input + ChatColor.RESET;
            } catch (NumberFormatException e) {
                return "" + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + input + ChatColor.RESET;
            }
        }
    }
}
