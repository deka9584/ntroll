package craft.ncraft.ntroll.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.model.UnluckyAction;
import craft.ncraft.ntroll.utils.EntityUtils;
import craft.ncraft.ntroll.utils.Utils;

public class UnluckyBlocksManager {
    private final NTroll plugin;
    private final Utils utils;

    private Map<String, List<UnluckyAction>> worldActions = new HashMap<>();

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

            List<UnluckyAction> actions = new ArrayList<UnluckyAction>();

            for (String key : section.getKeys(false)) {
                actions.add(new UnluckyAction(key, section.getInt(key)));
            }

            worldActions.put(worldEnv.toLowerCase(), actions);
        }
}

    public String getRandomAction(Player player) {
        List<UnluckyAction> actions = getActionListForPlayer(player);

        if (actions == null) {
            actions = worldActions.get("overworld");
            plugin.debugLog("Using default overworld break actions");
        }

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

    public List<UnluckyAction> getActionListForPlayer(Player player) {
        switch (player.getWorld().getEnvironment()) {
            case NETHER:
                return worldActions.get("nether");
            case THE_END:
                return worldActions.get("end");
            case NORMAL:
                return worldActions.get("overworld");
            default:
                return null;
        }
    }

    public List<String> getActionsNameList() {
        List<String> mappedActions = new ArrayList<>();

        worldActions.forEach((key, list) -> {
            mappedActions.add(key);

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

    public void spawnGenericMob(EntityType type, Player player, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(type, player, false);

        if (entity instanceof Mob) {
            updateMobTarget((Mob) entity, player, invisible);
        }
    }

    public void spawnMagmaCube(Player player, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.MAGMA_CUBE, player, false);

        if (entity instanceof MagmaCube) {
            MagmaCube magmacube = (MagmaCube) entity;
            magmacube.setSize(utils.getRandomInt(1, 2));
            updateMobTarget(magmacube, player, invisible);
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
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.PIG_ZOMBIE, player, false);

        if (entity instanceof PigZombie) {
            PigZombie pigZombie = (PigZombie) entity;
            pigZombie.setAngry(true);
            pigZombie.setBaby(baby);
            updateMobTarget(pigZombie, player, invisible);
        }
    }

    private void updateMobTarget(Mob mob, Player target, boolean invisible) {
        if (target != null && EntityUtils.isPlayerVulnerable(target)) {
            mob.setTarget(target);
        }

        if (invisible) {
            EntityUtils.addMobInvisibility(mob, 200);
        }
    }
}
