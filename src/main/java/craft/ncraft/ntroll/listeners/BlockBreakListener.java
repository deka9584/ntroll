package craft.ncraft.ntroll.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.managers.UnluckyBlocksManager;
import craft.ncraft.ntroll.utils.Utils;

public class BlockBreakListener implements Listener {
    private final NTroll plugin;
    private final Utils utils;

    public BlockBreakListener(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player == null || !plugin.isTrollEnabled() || !plugin.getTargetPlayerManager().isTargetPlayer(player.getName())) {
            return;
        }

        if (utils.chancePercent(plugin.getConfig().getInt("unluckyblock-break-chance"))) {
            UnluckyBlocksManager ubm = plugin.getUnluckyBlocksManager();
            Block block = event.getBlock();
            String action = ubm.getRandomAction(player.getWorld().getEnvironment());

            if (action == null) {
                plugin.debugLog("Unable to get action for unlucky block break");
                return;
            }

            plugin.debugLog("Unlucky action " + action + " for player " + player.getName());

            switch (action) {
                case "spawn-creeper":
                    ubm.spawnCreeper(player, false, false);
                    break;
                case "spawn-invisible-creeper":
                    ubm.spawnCreeper(player, false, true);
                    break;
                case "spawn-invisible-charged-creeper":
                    ubm.spawnCreeper(player, true, true);
                    break;
                case "spawn-enderman":
                    ubm.spawnEnderman(player, false);
                    break;
                case "spawn-invisible-enderman":
                    ubm.spawnEnderman(player, true);
                    break;
                case "spawn-silverfish":
                    ubm.spawnSilverfish(player, block, false);
                    break;
                case "spawn-invisible-silverfish":
                    ubm.spawnSilverfish(player, block, true);
                    break;
                case "spawn-baby-zombie":
                    ubm.spawnZombie(player, true, false);
                    break;
                case "spawn-pig-zombie":
                    ubm.spawnPigZombie(player, false, false);
                    break;
                case "spawn-baby-pig-zombie":
                    ubm.spawnPigZombie(player, true, false);
                    break;
                case "spawn-shulker":
                    ubm.spawnShulker(player, false);
                    break;
                case "spawn-shulker-bullet":
                    ubm.spawnShulkerBullet(player);
                    break;
                case "disable-drops":
                    event.setDropItems(false);
                    break;
                case "place-bedrock":
                    event.setCancelled(true);
                    block.setType(Material.BEDROCK);
                    utils.setMetadataValue(block, "ntroll-instant-break", true);
                    break;
                case "place-obsidian":
                    event.setCancelled(true);
                    block.setType(Material.OBSIDIAN);
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
