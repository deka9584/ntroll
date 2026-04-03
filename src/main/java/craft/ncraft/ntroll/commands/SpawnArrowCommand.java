package craft.ncraft.ntroll.commands;

import java.util.Map;

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
                cs.sendMessage(ChatColor.RED + "Arguments: [player] [params?]");
                return false;
            }

            Player player = plugin.getServer().getPlayer(args[0]);

            if (player == null) {
                cs.sendMessage(utils.getMsgFromCfg("player-not-found-msg"));
                return false;
            }

            Map<String, String> params = utils.extractCommandParams(args);
            Arrow arrow = utils.spawnArrowToPlayer(player);

            if (arrow == null) {
                cs.sendMessage(ChatColor.RED + "Unable to spawn arrow to player " + player.getName());
                return false;
            }

            if (params.containsKey("--critical")) {
                arrow.setCritical(true);
            }
            
            if (params.containsKey("--flame")) {
                String flame = params.get("--flame");

                arrow.setFireTicks(utils.validateNumericInput(flame, false)
                    ? Integer.parseInt(flame)
                    : 100
                );
            }

            if (params.containsKey("--damage")) {
                String damage = params.get("--damage");

                if (utils.validateNumericInput(damage, false)) {
                    arrow.setDamage(Double.parseDouble(damage));
                } else {
                    cs.sendMessage(utils.getMsgFromCfg("unable-to-apply-flag-msg")
                        .replaceAll("%flag%", "--damage")
                        .replaceAll("%value%", damage)
                    );
                }
            }

            cs.sendMessage(utils.getMsgFromCfg("spawn-success-msg")
                .replaceAll("%entity%", "ARROW")
                .replaceAll("%player%", player.getName())
            );

            return true;
        }

        return false;
    }
    
}
