package craft.ncraft.ntroll.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;

public class MainCommand implements CommandExecutor {
    private final NTroll plugin;
    private final Utils utils;

    public MainCommand(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @Override
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {
        String noperm = utils.getMsgFromCfg("no-permission-msg");
        String permission = command.getPermission();

        if (cs instanceof Player || cs instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                cs.sendMessage(plugin.getName() + " v." + plugin.getDescription().getVersion());
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!cs.hasPermission(permission + ".admin")) {
                    cs.sendMessage(noperm);
                    return false;
                }

                plugin.reload();
                return true;
            }

            if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on")) {
                if (!cs.hasPermission(permission + ".admin")) {
                    cs.sendMessage(noperm);
                    return false;
                }

                if (plugin.isTrollEnabled()) {
                    plugin.debugLog("Troll plugin already enabled");
                    cs.sendMessage(utils.getMsgFromCfg("troll-status-msg").replaceAll("%status%", "ON"));
                    return false;
                }

                plugin.getConfig().set("enable-troll", true);
                plugin.saveConfig();
                cs.sendMessage(utils.getMsgFromCfg("troll-enabled-msg"));
                return true;
            }

            if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off")) {
                if (!cs.hasPermission(permission + ".admin")) {
                    cs.sendMessage(noperm);
                    return false;
                }

                if (!plugin.isTrollEnabled()) {
                    plugin.debugLog("Troll plugin already disabled");
                    cs.sendMessage(utils.getMsgFromCfg("troll-status-msg").replaceAll("%status%", "OFF"));
                    return false;
                }

                plugin.getConfig().set("enable-troll", false);
                plugin.saveConfig();
                cs.sendMessage(utils.getMsgFromCfg("troll-disabled-msg"));
                return true;
            }

            if (args[0].equalsIgnoreCase("status")) {
                if (!cs.hasPermission(permission + ".admin")) {
                    cs.sendMessage(noperm);
                    return false;
                }

                cs.sendMessage(utils.getMsgFromCfg("troll-status-msg")
                    .replaceAll("%status%", plugin.isTrollEnabled() ? "ON" : "OFF")
                );

                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (!cs.hasPermission(permission + ".admin")) {
                    cs.sendMessage(noperm);
                    return false;
                }

                if (args.length == 1) {
                    cs.sendMessage(ChatColor.RED + "Usage: add [player]");
                    return false;
                }

                if (plugin.getTargetPlayerManager().addTrollPlayer(args[1])) {
                    cs.sendMessage(utils.getMsgFromCfg("added-player-msg").replaceAll("%player%", args[1]));
                    return true;
                }

                return false;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (!cs.hasPermission(permission + ".admin")) {
                    cs.sendMessage(noperm);
                    return false;
                }

                if (args.length == 1) {
                    cs.sendMessage(ChatColor.RED + "Usage: remove [player]");
                    return false;
                }

                if (plugin.getTargetPlayerManager().removeTrollPlayer(args[1])) {
                    cs.sendMessage(utils.getMsgFromCfg("removed-player-msg").replaceAll("%player%", args[1]));
                    return true;
                }

                return false;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (!cs.hasPermission(permission + ".admin")) {
                    cs.sendMessage(noperm);
                    return false;
                }

                String targetPlayers = String.join(" ,", plugin.getTargetPlayerManager().getPlayers());
                cs.sendMessage(utils.getMsgFromCfg("target-player-list").replaceAll("%list%", targetPlayers));
                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
                cs.sendMessage(utils.getMsgFromCfg("backup-reminder"));
                cs.sendMessage(ChatColor.GOLD + "Subcommands: on, off, status, add [player], remove [player], list");
                return true;
            }
        }

        return false;
    }

}
