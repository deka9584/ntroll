package craft.ncraft.ntroll.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityUtils {
    public static boolean addMobInvisibility(Mob mob, int duration) {
        return mob.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, false, false));
    }

    public static boolean isPlayerVulnerable(Player player) {
        if (player.isInvulnerable()) {
            return false;
        }

        GameMode gm = player.getGameMode();
        return gm == GameMode.ADVENTURE || gm == GameMode.SURVIVAL;
    }

    public static EntityType getEntityType(String name) {
        for (EntityType type : EntityType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getSpawnableEntityTypeList() {
        return Arrays.stream(EntityType.values())
            .filter(e -> e.isSpawnable())
            .map(EntityType::name)
            .collect(Collectors.toList());
    }

    public static boolean setMobScale(Mob mob, double value) {
        if (value >= 0.5 && value <= 10) {
            try {
                AttributeInstance scale = mob.getAttribute(Attribute.valueOf("GENERIC_SCALE"));
    
                if (scale != null) {
                    scale.setBaseValue(value);
                    return true;
                }
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("GENERIC_SCALE attribute is not available on this server version");
            }
        }
        
        return false;
    }

    public static boolean resetMobScale(Mob mob) {
        try {
            AttributeInstance scale = mob.getAttribute(Attribute.valueOf("GENERIC_SCALE"));

            if (scale != null && scale.getValue() != scale.getDefaultValue()) {
                scale.setBaseValue(scale.getDefaultValue());
                return true;
            }
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("GENERIC_SCALE attribute is not available on this server version");
        }

        return false;
    }
}
