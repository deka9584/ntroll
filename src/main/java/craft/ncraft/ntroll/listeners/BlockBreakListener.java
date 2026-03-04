package craft.ncraft.ntroll.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.Utils;

public class BlockBreakListener implements Listener {
    private final NTroll plugin;
    private final Utils utils;

    public BlockBreakListener(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player == null || !plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTargetPlayer(player.getName())) {
            return;
        }

        if (utils.chancePercent(plugin.getConfig().getInt("unluckyblock-break-chance"))) {
            String action = plugin.getUnluckyBlocksManager().getRandomAction();
            plugin.debugLog("Unlucky action " + action + " for player " + player.getName());

            switch (action) {
                case "spawn-creeper":
                    utils.spawnCreeperToPlayer(player, false, false);
                    break;
                case "spawn-invisible-creeper":
                    utils.spawnCreeperToPlayer(player, false, true);
                    break;
                case "spawn-invisible-charged-creeper":
                    utils.spawnCreeperToPlayer(player, true, true);
                    break;
                case "spawn-enderman":
                    utils.spawnEndermanToPlayer(player, false);
                    break;
                case "spawn-invisible-enderman":
                    utils.spawnEndermanToPlayer(player, true);
                    break;
                case "disable-drops":
                    event.setDropItems(false);
                    break;
                case "place-bedrock":
                    event.setCancelled(true);
                    event.getBlock().setType(Material.BEDROCK);
                    break;
                case "place-obsidian":
                    event.setCancelled(true);
                    event.getBlock().setType(Material.OBSIDIAN);
                    break;
                case "destroy-item":
                    player.getInventory().setItemInMainHand(null);
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                    break;
                case "explosion":
                    player.getWorld().createExplosion(player.getLocation(), 8F, true, true);
                    break;
                case "cancel-break":
                    event.setCancelled(true);
                    break;
                default:
                    plugin.getLogger().warning("Unknown action: " + action);
                    break;
            }
        }
    }
}
