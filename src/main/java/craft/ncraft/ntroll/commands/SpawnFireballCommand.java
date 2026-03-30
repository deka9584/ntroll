package craft.ncraft.ntroll.commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class SpawnFireballCommand implements CommandExecutor {
    private final NTroll plugin;
    private final Utils utils;

    public SpawnFireballCommand(NTroll plugin) {
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
                cs.sendMessage(ChatColor.RED + "Arguments: [player] [params?]");
                return false;
            }

            Player player = plugin.getServer().getPlayer(args[0]);

            if (player == null) {
                utils.getMsgFromCfg("player-not-found");
                return false;
            }

            Map<String, String> params = utils.extractCommandParams(args);
            Fireball fireball = utils.spawnFireballToPlayer(player, params.containsKey("--incendiary"));

            if (fireball == null) {
                cs.sendMessage(ChatColor.RED + "Unable to spawn arrow to player " + player.getName());
                return false;
            }

            cs.sendMessage(ChatColor.GREEN + "Spawned arrow to " + player.getName());
            return true;
        }

        return false;
    }
    
}
