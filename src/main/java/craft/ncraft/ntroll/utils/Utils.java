package craft.ncraft.ntroll.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import craft.ncraft.ntroll.NTroll;
import net.md_5.bungee.api.ChatColor;

public class Utils {
    private final NTroll plugin;

    public Utils(NTroll plugin) {
        this.plugin = plugin;
    }

    public boolean chancePercent(int percent) {
        return getRandomInt(1, 100) <= percent;
    }

    public String getMsgFromCfg(String path) {
        String prefix = plugin.getConfig().getString("prefix");
        return translateColorCodes(plugin.getConfig().getString(path).replaceAll("%prefix%", prefix));
    }

    public String translateColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public EntityType getEntityTypeByName(String name) {
        for (EntityType type : EntityType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public Creeper spawnCreeperToPlayer(Player player, boolean charged, boolean invisible) {
        Entity entity = spawnMobBehindPlayer(EntityType.CREEPER, player);

        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;

            if (charged) {
                creeper.setPowered(true);
            }

            if (invisible) {
                creeper.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1, false, false ));
            }
            
            if (player.getGameMode() == GameMode.SURVIVAL) {
                creeper.setTarget(player);
            }

            return creeper;
        }

        return null;
    }

    public Enderman spawnEndermanToPlayer(Player player, boolean invisible) {
        Entity entity = spawnMobBehindPlayer(EntityType.ENDERMAN, player);

        if (entity instanceof Enderman) {
            Enderman enderman = (Enderman) entity;

            if (invisible) {
                enderman.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1, false, false ));
            }

            if (player.getGameMode() == GameMode.SURVIVAL) {
                enderman.setTarget(player);
            }
            return enderman;
        }

        return null;
    }

    public Entity spawnMobBehindPlayer(EntityType entityType, Player player) {
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection();

        direction.setY(0);
        direction.normalize();

        Location behind = playerLoc.clone().subtract(direction.multiply(2));
        Location feet = behind.clone();
        Location head = behind.clone().add(0, 1, 0);
        Location ground = behind.clone().subtract(0, 1, 0);

        if (!feet.getBlock().getType().isSolid() && !head.getBlock().getType().isSolid() && ground.getBlock().getType().isSolid()) {
            plugin.debugLog("Spawning entity behind player, Entity: " + entityType.name());
            return player.getWorld().spawnEntity(behind, entityType);
        }
        
        plugin.debugLog("Spawning entity on player location, Entity: " + entityType.name());
        return player.getWorld().spawnEntity(playerLoc, entityType);
    }
}
