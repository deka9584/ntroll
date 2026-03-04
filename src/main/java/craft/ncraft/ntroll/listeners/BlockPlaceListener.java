package craft.ncraft.ntroll.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;

public class BlockPlaceListener implements Listener {
    private final NTroll plugin;
    private final Utils utils;

    public BlockPlaceListener(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (player == null || !plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTargetPlayer(player.getName())) {
            return;
        }

        if (player.isSneaking()) {
            if (utils.chancePercent(plugin.getConfig().getInt("cancel-block-place-sneaking-chance"))) {
                event.setCancelled(true);
                plugin.debugLog("Prevent player " + player.getName() + " from place block (sneaking)");
            }
        } else {
            if (utils.chancePercent(plugin.getConfig().getInt("cancel-block-place-chance"))) {
                event.setCancelled(true);
                plugin.debugLog("Prevent player " + player.getName() + " from place block");
            }
        }
    }
}
