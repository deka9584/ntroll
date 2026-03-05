package craft.ncraft.ntroll;

import java.util.logging.Level;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import craft.ncraft.ntroll.commands.MainCommand;
import craft.ncraft.ntroll.commands.SpawnMobBehindCommand;
import craft.ncraft.ntroll.listeners.BlockBreakListener;
import craft.ncraft.ntroll.listeners.BlockPlaceListener;
import craft.ncraft.ntroll.listeners.EntityTargetListener;
import craft.ncraft.ntroll.listeners.PlayerInteractListener;
import craft.ncraft.ntroll.managers.TargetPlayerManager;
import craft.ncraft.ntroll.managers.UnluckyBlocksManager;
import craft.ncraft.ntroll.utils.Utils;

public class NTroll extends JavaPlugin {
    private static NTroll plugin;

    private Utils utils;
    private UnluckyBlocksManager unluckyBlocksManager;
    private TargetPlayerManager targetPlayerManager;

    // This code is called after the server starts and after the /reload command
    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();

        utils = new Utils(this);
        unluckyBlocksManager = new UnluckyBlocksManager(this);
        targetPlayerManager = new TargetPlayerManager(this);

        getCommand("ntroll").setExecutor(new MainCommand(this));
        getCommand("spawnmobbehind").setExecutor(new SpawnMobBehindCommand(this));

        registerEvents();
        
        unluckyBlocksManager.loadActions();
        targetPlayerManager.loadPlayers();

        getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());
    }

    // This code is called before the server stops and after the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }

    public void debugLog(String msg) {
        if (getConfig().getBoolean("debug")) {
            getLogger().info(msg);
        }
    }

    public UnluckyBlocksManager getUnluckyBlocksManager() {
        return unluckyBlocksManager;
    }

    public TargetPlayerManager getTargetPlayerManager() {
        return targetPlayerManager;
    }

    public Utils getUtils() {
        return utils;
    }

    public boolean isTrollEnabled() {
        return getConfig().getBoolean("enable-troll");
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new BlockPlaceListener(this), this);
        pm.registerEvents(new PlayerInteractListener(this), this);
        pm.registerEvents(new EntityTargetListener(this), this);
    }

    public void reload() {
        reloadConfig();
        unluckyBlocksManager.loadActions();
        targetPlayerManager.loadPlayers();
    }

    public static NTroll getInstance() {
        return plugin;
    }
}
