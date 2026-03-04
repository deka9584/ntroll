package craft.ncraft.ntroll.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;

public class PlayerInteractListener implements Listener {
    private final NTroll plugin;
    private final Utils utils;

    public PlayerInteractListener(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player == null || !plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTargetPlayer(player.getName())) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            PlayerInventory inv = player.getInventory();
            boolean isRocket = inv.getItemInMainHand().getType() == Material.FIREWORK_ROCKET || inv.getItemInOffHand().getType() == Material.FIREWORK_ROCKET;
            
            if (isRocket && utils.chancePercent(plugin.getConfig().getInt("rocket-explosion-chance"))) {
                player.getWorld().createExplosion(player.getLocation(), 8F, true, true);
                plugin.debugLog("Explode rocket on player " + player.getName());
            }
        }
    }
}
