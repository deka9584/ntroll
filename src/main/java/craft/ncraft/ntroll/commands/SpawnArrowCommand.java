package craft.ncraft.ntroll.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class SpawnArrowCommand implements CommandExecutor {
    private final NTroll plugin;
    private final Utils utils;

    public SpawnArrowCommand(NTroll plugin) {
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

            if (args.length == 0) {
                cs.sendMessage(ChatColor.RED + "Arguments: [player]");
                return false;
            }

            Player player = plugin.getServer().getPlayer(args[0]);

            if (player == null) {
                cs.sendMessage(ChatColor.RED + "Player not found");
                return false;
            }

            Arrow arrow = utils.spawnArrowToPlayer(player);

            if (arrow == null) {
                cs.sendMessage(ChatColor.RED + "Unable to spawn arrow to player " + player);
                return false;
            }

            cs.sendMessage(ChatColor.GREEN + "Spawned arrow to " + player.getName());
            return true;
        }

        return false;
    }
    
}
