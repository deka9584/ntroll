package craft.ncraft.ntroll.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;

public class EntityTargetListener implements Listener {
    private final NTroll plugin;
    private final Utils utils;

    public EntityTargetListener(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();

        if (entity instanceof Mob && target instanceof Player) {
            Mob mob = (Mob) entity;
            Player targetPlayer = (Player) target;
            
            if (!plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTarget(targetPlayer.getName())) {
                return;
            }

            if (!mob.hasPotionEffect(PotionEffectType.SPEED) && utils.chancePercent(plugin.getConfig().getInt("faster-mob-chance"))) {
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2, false, false));
                plugin.debugLog("Added speed boost to mob " + mob.getType().name() + " for player " + targetPlayer.getName());
            }
        }
    }
}
