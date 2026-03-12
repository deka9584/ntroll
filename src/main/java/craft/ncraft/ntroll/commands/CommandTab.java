package craft.ncraft.ntroll.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.util.StringUtil;

import craft.ncraft.ntroll.NTroll;

public class CommandTab implements TabCompleter {
    private final NTroll plugin;
    private final String[] ntroll_subcommand = {
        "reload",
        "on",
        "off",
        "enable",
        "disable",
        "status",
        "add",
        "remove",
        "list",
        "break",
        "break-actions",
        "help"
    };
    private final String[] spawnmobbehind_params = {
        "--force",
        "--invisible",
        "--powered",
        "--autotarget"
    };

    public CommandTab(NTroll plugin) {
        this.plugin = plugin;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
                                      String alias, String[] args) {
        List<String> completeSubCommand = new ArrayList<>();

        switch (command.getName()) {
            case "ntroll":
                if (args.length == 1 && args[0] != null) {
                    StringUtil.copyPartialMatches(args[0], Arrays.asList(ntroll_subcommand), completeSubCommand);
                } else if (args.length == 2 && args[1] != null) {
                    if (args[0].equalsIgnoreCase("add")) {
                        StringUtil.copyPartialMatches(args[1], getNotTargetPlayerNames(), completeSubCommand);
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        StringUtil.copyPartialMatches(args[1], plugin.getTargetPlayerManager().getPlayers(), completeSubCommand);
                    }
                }
                break;
            case "spawnmobbehind":
                if (args.length == 1 && args[0] != null) {
                    StringUtil.copyPartialMatches(args[0], getSpawnableEntityNames(), completeSubCommand);
                } else if (args.length == 2 && args[1] != null) {
                    StringUtil.copyPartialMatches(args[1], getOnlinePlayerNames(), completeSubCommand);
                } else if (args.length >= 3 && args[2] != null) {
                    StringUtil.copyPartialMatches(args[args.length - 1], Arrays.asList(spawnmobbehind_params), completeSubCommand);
                }
                break;
            case "spawnarrow":
                if (args.length == 1 && args[0] != null) {
                    StringUtil.copyPartialMatches(args[0], getOnlinePlayerNames(), completeSubCommand);
                }
        }

        return completeSubCommand;
	}

    private List<String> getOnlinePlayerNames() {
        return plugin.getServer().getOnlinePlayers()
            .stream()
            .map(p -> p.getName())
            .collect(Collectors.toList());
    }

    private List<String> getNotTargetPlayerNames() {
        return plugin.getServer().getOnlinePlayers()
            .stream()
            .map(p -> p.getName())
            .filter(n -> !plugin.getTargetPlayerManager().isTargetPlayer(n))
            .collect(Collectors.toList());
    }

    public List<String> getSpawnableEntityNames() {
        return Arrays.stream(EntityType.values())
            .filter(e -> e.isSpawnable())
            .map(EntityType::name)
            .collect(Collectors.toList());
    }
}
