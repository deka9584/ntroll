package craft.ncraft.ntroll.commands;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import craft.ncraft.ntroll.utils.EntityUtils;
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
                Map<String, String> params = utils.extractCommandParams(args);
                EntityType entityType = EntityUtils.getEntityType(args[0]);

                if (entityType == null || !entityType.isSpawnable()) {
                    cs.sendMessage(ChatColor.RED + "Entity not found or not spawnable");
                    return false;
                }

                Player target = plugin.getServer().getPlayer(args[1]);

                if (target == null) {
                    utils.getMsgFromCfg("player-not-found");
                    return false;
                }

                boolean forceSafeLocation = params.containsKey("--force");
                Entity entity = utils.spawnEntityBehindPlayer(entityType, target, forceSafeLocation);

                if (entity == null) {
                    cs.sendMessage(ChatColor.RED + "Unable to spawn entity in safe location");
                    return false;
                }
                
                if (entity instanceof Mob) {
                    Mob mob = (Mob) entity;

                    if (params.containsKey("--invisible")) {
                        EntityUtils.addMobInvisibility(mob, 200);
                    }

                    if (params.containsKey("--powered") && mob.getType() == EntityType.CREEPER) {
                        ((Creeper) mob).setPowered(true);
                    }

                    if (params.containsKey("--autotarget") && EntityUtils.isPlayerVulnerable(target)) {
                        mob.setTarget(target);
                    }

                    if (params.containsKey("--scale")) {
                        String scale = params.get("--scale");

                        if (scale.isEmpty() || !StringUtils.isNumeric(scale) || !EntityUtils.setMobScale(mob, Double.parseDouble(scale))) {
                            utils.getMsgFromCfg("unable-to-apply-flag").replace("%flag%", "--scale").replace("%value%", scale);
                        }
                    }
                }

                cs.sendMessage(ChatColor.GREEN + "Spawned entity " + entityType.name() + " to player " + target.getName());
                return true;
            }

            cs.sendMessage(ChatColor.RED + "Arguments: [entity] [player] [params?]");
            return false;
        }

        return false;
    }
    
}
