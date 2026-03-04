package craft.ncraft.ntroll.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.model.UnluckyAction;
import craft.ncraft.ntroll.utils.Utils;

public class UnluckyBlocksManager {
    private final NTroll plugin;
    private final Utils utils;
    private List<UnluckyAction> actions = new ArrayList<>();

    public UnluckyBlocksManager(NTroll plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    public void loadActions() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("unluckyblock-break-actions");

        if (section != null) {
            actions.clear();

            for (String key : section.getKeys(false)) {
                int chance = plugin.getConfig().getInt("unluckyblock-break-actions." + key);
                actions.add(new UnluckyAction(key, chance));
            }
        }
    }

    public String getRandomAction() {
        int totalWeight = actions.stream().mapToInt(UnluckyAction::getChange).sum();
        int randomNumber = utils.getRandomInt(0, totalWeight);
        int currWeight = 0;

        for (UnluckyAction ua : actions) {
            currWeight += ua.getChange();

            if (randomNumber < currWeight) {
                return ua.getName();
            }
        }

        return null;
    }
}
