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
        String permission = command.getPermission();
        String noperm = utils.getMsgFromCfg("no-permission-msg");

        if (cs instanceof Player || cs instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                cs.sendMessage(plugin.getName() + " v." +
                        plugin.getDescription().getVersion());
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "reload":
                    if (!hasPermission(cs, permission + ".admin", noperm))
                        return false;
                    plugin.reload();
                    cs.sendMessage(utils.getMsgFromCfg("config-reloaded-msg"));
                    return true;

                case "on":
                    if (!hasPermission(cs, permission + ".admin", noperm) || checkTrollBeEnable(cs, false))
                        return false;
                    plugin.getConfig().set("enable-troll", true);
                    plugin.saveConfig();
                    cs.sendMessage(utils.getMsgFromCfg("troll-enabled-msg"));
                    return true;

                case "off":
                    if (!hasPermission(cs, permission + ".admin", noperm) || checkTrollBeEnable(cs, true))
                        return false;
                    plugin.getConfig().set("enable-troll", false);
                    plugin.saveConfig();
                    cs.sendMessage(utils.getMsgFromCfg("troll-disabled-msg"));
                    return true;

                case "status":
                    if (!hasPermission(cs, permission + ".admin", noperm))
                        return false;
                    cs.sendMessage(utils.getMsgFromCfg("troll-status-msg")
                            .replaceAll("%status%", plugin.isTrollEnabled() ? "ON" : "OFF"));
                    return true;

                case "add":
                    if (!hasPermission(cs, permission + ".admin", noperm))
                        return false;
                    if (args.length == 1) {
                        cs.sendMessage(ChatColor.RED + "Usage: add [player]");
                        return false;
                    }
                    if (plugin.getTargetPlayerManager().addTrollPlayer(args[1])) {
                        cs.sendMessage(utils.getMsgFromCfg("added-player-msg")
                                .replaceAll("%player%", args[1]));
                        return true;
                    }
                    return false;

                case "remove":
                    if (!hasPermission(cs, permission + ".admin", noperm))
                        return false;
                    if (args.length == 1) {
                        cs.sendMessage(ChatColor.RED + "Usage: remove [player]");
                        return false;
                    }
                    if (plugin.getTargetPlayerManager().removeTrollPlayer(args[1])) {
                        cs.sendMessage(utils.getMsgFromCfg("removed-player-msg")
                                .replaceAll("%player%", args[1]));
                        return true;
                    }
                    return false;

                case "list":
                    if (!hasPermission(cs, permission + ".admin", noperm))
                        return false;
                    String targetPlayers = String.join(" ,", plugin.getTargetPlayerManager()
                            .getPlayers());
                    cs.sendMessage(utils.getMsgFromCfg("target-player-list")
                            .replaceAll("%list%", targetPlayers));
                    return true;

                case "break-actions":
                    if (!hasPermission(cs, permission + ".admin", noperm))
                        return false;
                    String breakActions = String.join("\n", plugin.getUnluckyBlocksManager()
                            .getActionsList());
                    cs.sendMessage(utils.getMsgFromCfg("break-action-list")
                            .replaceAll("%list%", breakActions));
                    return true;

                case "help":
                    cs.sendMessage(utils.getMsgFromCfg("backup-reminder"));
                    cs.sendMessage(ChatColor.GOLD +
                            "Subcommands: on, off, status, add [player], remove [player], " +
                            "list, break-actions");
                    return true;

                default:
                    cs.sendMessage(ChatColor.RED + "Invalid subcommand");
                    return false;
            }
        }

        return false;
    }

    private boolean hasPermission(CommandSender cs, String permission, String noperm) {
        if (!cs.hasPermission(permission + ".admin")) {
            cs.sendMessage(noperm);
            return false;
        }

        return true;
    }

    private boolean checkTrollBeEnable(CommandSender cs, boolean shouldBeEnable) {
        if (plugin.isTrollEnabled() ^ shouldBeEnable) {
            plugin.debugLog("Troll plugin already " +
                    (plugin.isTrollEnabled() ? "enabled" : "disabled"));
            cs.sendMessage(utils.getMsgFromCfg("troll-status-msg")
                           .replaceAll("%status%", plugin.isTrollEnabled() ? "ON" : "OFF"));
            return true;
        }

        return false;
    }
}
