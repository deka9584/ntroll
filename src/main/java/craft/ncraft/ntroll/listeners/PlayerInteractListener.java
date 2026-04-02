package craft.ncraft.ntroll.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

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
        Action action = event.getAction();

        if (!plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTargetPlayer(player.getName())) {
            return;
        }

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            Location loc = player.getLocation();

            if (item != null && item.getType() == Material.FIREWORK_ROCKET) {
                if (player.isGliding()) {
                    int breakMinHeight = plugin.getConfig().getInt("break-elytra.min-height");
    
                    if (breakMinHeight >= 0 && breakMinHeight < loc.getY() && utils.chancePercent(plugin.getConfig().getInt("break-elytra.chance"))) {
                        player.getInventory().setChestplate(null);
                        player.playSound(loc, Sound.ENTITY_ITEM_BREAK, 1, 1);
                        plugin.debugLog("Breaking elytra to player " + player.getName());
                    }
                }
                
                if (utils.chancePercent(plugin.getConfig().getInt("rocket-explosion-chance"))) {
                    player.getWorld().createExplosion(loc, 4F, false, true);
                    plugin.debugLog("Explode rocket on player " + player.getName());
                }
            }
        }

        if (action == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            if (block != null && block.getType() == Material.BEDROCK) {
                MetadataValue instantBreakMeta = utils.extractMetadataValue(block, "ntroll-instant-break");

                if (instantBreakMeta != null && instantBreakMeta.asBoolean()) {
                    block.removeMetadata("ntroll-instant-break", plugin);
                    block.breakNaturally();
                }
            }
        }
    }
}
