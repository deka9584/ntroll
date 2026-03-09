package craft.ncraft.ntroll.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.model.UnluckyAction;
import craft.ncraft.ntroll.utils.Utils;

public class UnluckyBlocksManager {
    private final NTroll plugin;
    private final Utils utils;

    private Map<Environment, List<UnluckyAction>> worldActions = new HashMap<>();

    public UnluckyBlocksManager(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    public void loadActions() {
        ConfigurationSection root = plugin.getConfig().getConfigurationSection("unluckyblock-break-actions");

        if (root == null) return;

        for (String worldEnv : root.getKeys(false)) {
            ConfigurationSection section = root.getConfigurationSection(worldEnv);

            if (section == null) continue;

            List<UnluckyAction> actions;

            switch (worldEnv.toLowerCase()) {
                case "overworld":
                    actions = worldActions.get(Environment.NORMAL);
                    break;
                case "nether":
                    actions = worldActions.get(Environment.NETHER);
                    break;
                case "end":
                    actions = worldActions.get(Environment.THE_END);
                    break;
                default:
                    plugin.debugLog("Invalid world env " + worldEnv + " in unluckyblock-break-actions");
                    continue;
            }

            actions.clear();

            for (String key : section.getKeys(false)) {
                actions.add(new UnluckyAction(key, section.getInt(key)));
            }
        }
}

    public String getRandomAction(Environment worldEnv) {
        List<UnluckyAction> actions = worldActions.get(worldEnv);

        int totalWeight = actions.stream().mapToInt(UnluckyAction::getChance).sum();
        int randomNumber = utils.getRandomInt(0, totalWeight - 1);
        int currWeight = 0;

        for (UnluckyAction ua : actions) {
            currWeight += ua.getChance();

            if (randomNumber < currWeight) {
                return ua.getName();
            }
        }

        return null;
    }

    public List<String> getActionsList() {
        List<String> mappedActions = new ArrayList<>();

        worldActions.forEach((key, list) -> {
            mappedActions.add(key.name());

            for (UnluckyAction ua : list) {
            mappedActions.add(ua.getName() + ":" + ua.getChance());
        }
        });

        return mappedActions;
    }

    public void spawnCreeper(Player player, boolean charged, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.CREEPER, player, false);

        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;

            if (charged) {
                creeper.setPowered(true);
            }

            updateMobTarget(creeper, player, invisible);
        }
    }

    public void spawnEnderman(Player player, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.ENDERMAN, player, false);

        if (entity instanceof Enderman) {
            updateMobTarget((Enderman) entity, player, invisible);
        }
    }

    public void spawnSilverfish(Player player, Block block, boolean invisible) {
        Entity entity;

        if (block != null) {
            entity = utils.spawnEntityOnBlock(EntityType.SILVERFISH, block);
        } else {
            entity = utils.spawnEntityBehindPlayer(EntityType.SILVERFISH, player, false);
        }

        if (entity instanceof Mob) {
            updateMobTarget((Mob) entity, player, invisible);
        }
    }

    public void spawnZombie(Player player, boolean baby, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.ZOMBIE, player, false);

        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            zombie.setBaby(baby);
            updateMobTarget(zombie, player, invisible);
        }
    }

    public void spawnPigZombie(Player player, boolean baby, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.PIG_ZOMBIE, player, invisible);

        if (entity instanceof PigZombie) {
            PigZombie pigZombie = (PigZombie) entity;
            pigZombie.setAngry(true);
            pigZombie.setBaby(baby);
            updateMobTarget(pigZombie, player, invisible);
        }
    }

    private void updateMobTarget(Mob mob, Player target, boolean invisible) {
        if (target != null && utils.isPlayerVulnerable(target)) {
            mob.setTarget(target);
        }

        if (invisible) {
            utils.addMobInvisibility(mob, 200);
        }
    }
}
