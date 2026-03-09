package craft.ncraft.ntroll.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
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

    public boolean isPlayerVulnerable(Player player) {
        if (player.isInvulnerable()) {
            return false;
        }

        GameMode gm = player.getGameMode();
        return gm == GameMode.ADVENTURE || gm == GameMode.SURVIVAL;
    }

    public boolean chancePercent(int percent) {
        return getRandomInt(1, 100) <= percent;
    }

    public MetadataValue extractMetadataValue(Metadatable metadatable, String key) {
        if (metadatable.hasMetadata(key)) {
            for (MetadataValue value : metadatable.getMetadata(key)) {
                if (value.getOwningPlugin() == plugin) {
                    return value;
                }
            }
        }

        return null;
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

    public Entity spawnEntityOnBlock(EntityType entityType, Block block) {
        return block.getWorld().spawnEntity(block.getLocation().add(0.5, 0, 0.5), entityType);
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
            plugin.debugLog("Spawning entity behind player, Entity: " + entityType.name());
            return player.getWorld().spawnEntity(behind.add(0.5, 0, 0.5), entityType);
        }
        
        if (force) {
            plugin.debugLog("Spawning entity on player location, Entity: " + entityType.name());
            return player.getWorld().spawnEntity(playerLoc, entityType);
        }

        plugin.debugLog("Prevented spawning entity in unsafe location, Entity: " + entityType.name());
        return null;
    }

    public void setMetadataValue(Metadatable metadatable, String key, Object value) {
        metadatable.setMetadata(key, new FixedMetadataValue(plugin, value));
    }
}
