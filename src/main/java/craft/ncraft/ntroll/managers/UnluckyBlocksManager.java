package craft.ncraft.ntroll.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.model.UnluckyAction;
import craft.ncraft.ntroll.utils.Utils;

public class UnluckyBlocksManager {
    private final NTroll plugin;
    private final Utils utils;
    private List<UnluckyAction> actions = new ArrayList<>();

    public UnluckyBlocksManager(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    public void loadActions() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("unluckyblock-break-actions");

        if (section != null) {
            actions.clear();

            for (String key : section.getKeys(false)) {
                int chance = plugin.getConfig().getInt("unluckyblock-break-actions." + key);
                actions.add(new UnluckyAction(key, chance));
            }
        }
    }

    public String getRandomAction() {
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

        for (UnluckyAction ua : actions) {
            mappedActions.add(ua.getName() + ":" + ua.getChance());
        }

        return mappedActions;
    }

    public void spawnCreeper (Player player, boolean charged, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.CREEPER, player, false);

        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;

            if (charged) {
                creeper.setPowered(true);
            }

            if (invisible) {
                utils.addMobInvisibility(creeper, 200);
            }
            
            if (player.getGameMode() == GameMode.SURVIVAL) {
                creeper.setTarget(player);
            }
        }
    }

    public void spawnEnderman(Player player, boolean invisible) {
        Entity entity = utils.spawnEntityBehindPlayer(EntityType.ENDERMAN, player, false);

        if (entity instanceof Enderman) {
            Enderman enderman = (Enderman) entity;

            if (invisible) {
                utils.addMobInvisibility(enderman, 200);
            }

            if (player.getGameMode() == GameMode.SURVIVAL) {
                enderman.setTarget(player);
            }
        }
    }

    public void spawnSilverfish(Player player, Block block, boolean invisible) {
        Entity entity;

        if (block != null) {
            entity = block.getWorld().spawnEntity(block.getLocation().add(0.5, 0, 0.5), EntityType.SILVERFISH);
        } else {
            entity = utils.spawnEntityBehindPlayer(EntityType.SILVERFISH, player, false);
        }

        if (entity instanceof Mob) {
            Mob mob = (Mob) entity;

            if (invisible) {
                utils.addMobInvisibility(mob, 200);
            }

            if (player.getGameMode() == GameMode.SURVIVAL) {
                mob.setTarget(player);
            }
        }
    }
}
