package craft.ncraft.ntroll.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import craft.ncraft.ntroll.NTroll;
import craft.ncraft.ntroll.utils.EntityUtils;

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
    private final String[] spawnarrow_params = {
        "--critical",
        "--flame"
    };
    private final String[] spawnfireball_params = {
        "--incendiary"
    };
    private final List<String> entity_types = EntityUtils.getSpawnableEntityTypeList();

    public CommandTab(NTroll plugin) {
        this.plugin = plugin;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
                                      String alias, String[] args) {
        List<String> completeSubCommand = new ArrayList<>();

        switch (command.getName()) {
            case "ntroll":
                if (args.length == 1) {
                    return StringUtil.copyPartialMatches(args[0], Arrays.asList(ntroll_subcommand), completeSubCommand);
                } 
                
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("add")) {
                        return null;
                    }
                    
                    if (args[0].equalsIgnoreCase("remove")) {
                        return StringUtil.copyPartialMatches(args[1], plugin.getTargetPlayerManager().getPlayers(), completeSubCommand);
                    }
                }
                break;
            case "spawnmobbehind":
                if (args.length == 1) {
                    return StringUtil.copyPartialMatches(args[0], entity_types, completeSubCommand);
                } 
                 
                if (args.length == 2) {
                    return null;
                } 
                
                if (args.length >= 3) {
                    return filterParams(args, spawnmobbehind_params, completeSubCommand);
                }
                break;
            case "spawnarrow":
                if (args.length == 1) {
                    return null;
                }

                if (args.length >= 1) {
                    return filterParams(args, spawnarrow_params, completeSubCommand);
                }
                break;
            case "spawnshulkerbullet":
                if (args.length == 1) return null;
                break;
            case "spawnfireball":
                if (args.length == 1) {
                    return null;
                }

                if (args.length >= 1) {
                    return filterParams(args, spawnfireball_params, completeSubCommand);
                }
                break;
        }

        return completeSubCommand;
	}

    private List<String> filterParams(String[] args, String[] params, List<String> dstList) {
        int lastArgI = args.length - 1;
        String token = args[lastArgI];

        for (String param : params) {
            boolean written = false;

            for (int i = 0; i < lastArgI; i++) {
                if (param.equals(args[i])) {
                    written = true;
                    break;
                }
            }

            if (!written && param.startsWith(token)) {
                dstList.add(param);
            }
        }

        return dstList;
    }
}
