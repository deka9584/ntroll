package craft.ncraft.ntroll.commands;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
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

            if (args.length > 1) {
                Set<String> params = Stream.of(args).filter(str -> str.startsWith("--")).collect(Collectors.toSet());
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

                boolean forceSafeLocation = params.contains("--force");
                Entity entity = utils.spawnEntityBehindPlayer(entityType, target, forceSafeLocation);

                if (entity == null) {
                    cs.sendMessage(ChatColor.RED + "Unable to spawn entity in safe location");
                    return false;
                }
                
                if (entity instanceof Mob) {
                    Mob mob = (Mob) entity;

                    if (params.contains("--invisible")) {
                        utils.addMobInvisibility(mob, 200);
                    }

                    if (params.contains("--powered") && mob.getType() == EntityType.CREEPER) {
                        ((Creeper) mob).setPowered(true);
                    }

                    if (params.contains("--autotarget") && utils.isPlayerVulnerable(target)) {
                        mob.setTarget(target);
                    }
                }

                cs.sendMessage(ChatColor.GREEN + "Spawned entity " + entityType.name() + " to player " + target.getName());
                return true;
            }

            cs.sendMessage(ChatColor.RED + "Arguments: [entity] [player], Optional params: --force, --invisible, --powered, --autotarget");
            return false;
        }

        return false;
    }
    
}
