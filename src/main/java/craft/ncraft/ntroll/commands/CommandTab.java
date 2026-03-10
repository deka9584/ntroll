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
                if (args.length == 1 && args[0] != null)
                    StringUtil.copyPartialMatches(args[0], Arrays.asList(ntroll_subcommand), completeSubCommand);
                break;
            case "spawnmobbehind":
                List<String> entityTypesStrings = Arrays.stream(EntityType.values())
                        .filter(e -> e.isAlive())
                        .map(EntityType::name)
                        .collect(Collectors.toList());

                List<String> onlineTargetNames = plugin.getServer().getOnlinePlayers()
                        .stream()
                        .map(p -> p.getName())
                        .filter(p -> plugin.getTargetPlayerManager().isTargetPlayer(p))
                        .collect(Collectors.toList());

                StringUtil.copyPartialMatches(args[0], entityTypesStrings, completeSubCommand);

                if (args.length == 2 && args[1] != null) {
                    completeSubCommand.clear();
                    StringUtil.copyPartialMatches(args[1], onlineTargetNames, completeSubCommand);
                } else if (args.length == 3 && args[2] != null) {
                    completeSubCommand.clear();
                    StringUtil.copyPartialMatches(args[2], Arrays.asList(spawnmobbehind_params), completeSubCommand);
                }
                break;
        }

        return completeSubCommand;
	}
}
