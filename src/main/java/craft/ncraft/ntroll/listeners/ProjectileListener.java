package craft.ncraft.ntroll.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;

public class ProjectileListener implements Listener {
    private final NTroll plugin;
    private final Utils utils;

    public ProjectileListener(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) projectile;
            ProjectileSource shooter = pearl.getShooter();

            if (shooter instanceof Player) {
                Player player = (Player) shooter;

                if (!plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTargetPlayer(player.getName())) {
                    return;
                }

                if (utils.chancePercent(plugin.getConfig().getInt("enderpearl.endermite-chance"))) {
                    Location loc = pearl.getLocation();

                    loc.setYaw(loc.getYaw() + utils.getRandomInt(-10, 10));
                    loc.setPitch(loc.getPitch() + utils.getRandomInt(-10, 10));

                    Vector dir = loc.getDirection();
                    pearl.setVelocity(dir.multiply(pearl.getVelocity().length()));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) projectile;
            ProjectileSource shooter = pearl.getShooter();
            
            if (shooter instanceof Player) {
                Player player = (Player) shooter;

                if (!plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTargetPlayer(player.getName())) {
                    return;
                }

                if (utils.chancePercent(plugin.getConfig().getInt("enderpearl.endermite-chance"))) {
                    pearl.getWorld().spawnEntity(player.getLocation(), EntityType.ENDERMITE);
                }
            }
        }
    }
}
