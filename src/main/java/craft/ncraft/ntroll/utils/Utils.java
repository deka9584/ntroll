package craft.ncraft.ntroll.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
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

    public boolean addMobInvisibility(Mob mob, int duration) {
        return mob.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, false, false));
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

    public Entity spawnEntityBehindPlayer(EntityType entityType, Player player, boolean force) {
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection();

        direction.setY(0);
        direction.normalize();

        Location behind = playerLoc.clone().subtract(direction.multiply(2));
        Location feet = behind.clone();
        Location head = behind.clone().add(0, 1, 0);
        Location ground = behind.clone().subtract(0, 1, 0);

        if (feet.getBlock().isPassable() && head.getBlock().isPassable() && ground.getBlock().getType().isSolid()) {
            behind.add(0.5, 0, 0.5);
            plugin.debugLog("Spawning entity behind player, Entity: " + entityType.name());
            return player.getWorld().spawnEntity(behind, entityType);
        }
        
        if (force) {
            plugin.debugLog("Spawning entity on player location, Entity: " + entityType.name());
            return player.getWorld().spawnEntity(playerLoc, entityType);
        }

        plugin.debugLog("Prevented spawning entity in unsafe location, Entity: " + entityType.name());
        return null;
    }
}
