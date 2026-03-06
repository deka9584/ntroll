package craft.ncraft.ntroll.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class SpawnMobBehindCommand implements CommandExecutor {
    private final NTroll plugin;
    private final Utils utils;

    public SpawnMobBehindCommand(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @Override
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {
        if (cs instanceof Player || cs instanceof ConsoleCommandSender) {
            if (!cs.hasPermission(command.getPermission())) {
                cs.sendMessage(utils.getMsgFromCfg("no-permission-msg"));
                return false;
            }

            if (args.length == 2) {
                EntityType entityType = utils.getEntityTypeByName(args[0]);

                if (entityType == null || !entityType.isSpawnable()) {
                    cs.sendMessage(ChatColor.RED + "Entity not found or not spawnable");
                    return false;
                }

                Player target = plugin.getServer().getPlayer(args[1]);

                if (target == null) {
                    cs.sendMessage(ChatColor.RED + "Player not found");
                    return false;
                }

                if (utils.spawnEntityBehindPlayer(entityType, target, false) != null) {
                    cs.sendMessage(ChatColor.GREEN + "Spawned entity " + entityType.name() + " to player " + target.getName());
                    return true;
                }

                cs.sendMessage(ChatColor.RED + "Unable to spawn entity in safe location");
                return false;
            }

            cs.sendMessage(ChatColor.RED + "Arguments: [entity] [player]");
            return false;
        }

        return false;
    }
    
}
