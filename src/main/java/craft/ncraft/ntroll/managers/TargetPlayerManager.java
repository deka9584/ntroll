package craft.ncraft.ntroll.managers;

import java.util.List;

import craft.ncraft.ntroll.NTroll;

public class TargetPlayerManager {
    private final NTroll plugin;
    private List<String> players;

    public TargetPlayerManager(NTroll plugin) {
        this.plugin = plugin;
    }

    public void loadPlayers() {
        players = plugin.getConfig().getStringList("target-players");
    }

    public boolean isTargetPlayer(String name) {
        if (players != null) {
            return players.contains(name);
        }
        return false;
    }

    public boolean addTrollPlayer(String name) {
        if (players != null && !players.contains(name)) {
            players.add(name);
            savePlayers();
            return true;
        }

        return false;
    }

    public boolean removeTrollPlayer(String name) {
        if (players != null && players.remove(name)) {
            savePlayers();
            return true;
        }

        return false;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void savePlayers() {
        plugin.getConfig().set("target-players", players);
        plugin.saveConfig();
    }
}
