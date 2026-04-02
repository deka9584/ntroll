package craft.ncraft.ntroll.managers;

import java.util.List;

import craft.ncraft.ntroll.NTroll;

public class TargetPlayerManager {
    private final NTroll plugin;
    private List<String> targets;

    public TargetPlayerManager(NTroll plugin) {
        this.plugin = plugin;
    }

    public void loadPlayers() {
        targets = plugin.getConfig().getStringList("target-players");
    }

    public boolean isTargetPlayer(String name) {
        return targets != null && targets.contains(name);
    }

    public boolean addTrollPlayer(String name) {
        if (targets != null && !targets.contains(name)) {
            targets.add(name);
            savePlayers();
            return true;
        }

        return false;
    }

    public boolean removeTrollPlayer(String name) {
        if (targets != null && targets.remove(name)) {
            savePlayers();
            return true;
        }

        return false;
    }

    public List<String> getTargetList() {
        return targets;
    }

    public void savePlayers() {
        plugin.getConfig().set("target-players", targets);
        plugin.saveConfig();
    }
}
