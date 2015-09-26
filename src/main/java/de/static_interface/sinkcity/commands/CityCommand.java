package de.static_interface.sinkcity.commands;

import static de.static_interface.sinkcity.LanguageConfiguration.getLangString;
import static de.static_interface.sinkcity.SinkCity.prefix;

import java.util.List;
import java.util.UUID;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.static_interface.sinkcity.SinkCity;
import de.static_interface.sinkcity.Utils;
import de.static_interface.sinklibrary.api.command.SinkCommand;
import de.static_interface.sinklibrary.util.BukkitUtil;

public class CityCommand extends SinkCommand {

    public CityCommand(SinkCity sinkCity) {
        super(sinkCity);
        String usage = "";
        String s = prefix + "/city ";
        usage += prefix;
        usage += Utils.format(getLangString("Help.AvailableCommands"), true);
        usage += "\n";
        usage += (s + "create");
        usage += "\n";
        usage += (s + "delete");
        usage += "\n";
        usage += (s + "list");
        usage += "\n";
        usage += (s + "join");
        usage += "\n";
        usage += (s + "here");
        usage += "\n";
        usage += (s + "info");
        usage += "\n";
        usage += (s + "teleport");
        usage += "\n";
        usage += (s + "set");
        usage += "\n";
        usage += (s + String.format("help <%s>", getLangString("Help.Command")));
        Options options = new Options();
        Option create = Option.builder("create").desc(getLangString("SinkCity.City.Help.Create")).build();
        Option delete = Option.builder("delete").desc(getLangString("SinkCity.City.Help.Delete")).build();
        Option list = Option.builder("list").desc(getLangString("SinkCity.City.Help.List")).build();
        Option here = Option.builder("here").desc(getLangString("SinkCity.City.Help.Here")).build();
        Option info = Option.builder("info").desc(getLangString("SinkCity.City.Help.Info")).build();
        Option set = Option.builder("set").desc(getLangString("SinkCity.City.Help.Set")).build();
        options.addOption(create);
        options.addOption(delete);
        options.addOption(list);
        options.addOption(here);
        options.addOption(info);
        options.addOption(set);
        this.getCommandOptions().setCliOptions(options);
        //        this.setUsage(usage);
    }

    private static boolean isTeleportLocationValid(Location loc) {
        Block feetBlock = loc.getWorld().getBlockAt(loc);
        Block headBlock = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
        Block aboveHeadBlock = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 2, loc.getBlockZ());
        Block belowFeetBlock = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        // The blocks where feet and head will be have to be empty, as well as the block above the head
        // The block below the feet may neither be a liquid nor empty.
        return (feetBlock.isEmpty() && headBlock.isEmpty() && aboveHeadBlock.isEmpty() && (!belowFeetBlock.isEmpty() && !belowFeetBlock.isLiquid()));
    }

    private static String formatResidentsList(List<UUID> residentsList, List<UUID> assistantsList, UUID mayor, int limit) {
        String residents = "";

        for (int x = 0; x < residentsList.size(); x++) {
            if (mayor.equals(residentsList.get(x))) {
                residents += ChatColor.RED + Bukkit.getOfflinePlayer(residentsList.get(x)).getName();
            } else if (assistantsList.contains(residentsList.get(x))) {
                residents += ChatColor.GREEN + Bukkit.getOfflinePlayer(residentsList.get(x)).getName();
            } else {
                residents += Bukkit.getOfflinePlayer(residentsList.get(x)).getName();
            }

            if (x < (residentsList.size() - 1))
                residents += ", ";
            if (x > limit)
                residents += "...";
        }

        return residents;
    }

    private static void sendHelp(CommandSender commandSender, String label, String subCommand) {
        if (subCommand == null)
            return;

        String s = Utils.format(prefix + "/" + label + " ", false);
        switch (subCommand) {
            case "create":
                commandSender.sendMessage(s + "create <" + getLangString("Help.City") + "> [" + getLangString("Help.Mayor") + "]: " + getLangString("SinkCity.City.Help.Create"));
                return;
            case "delete":
                commandSender.sendMessage(s + "delete <" + getLangString("Help.City") + ">: " + getLangString("SinkCity.City.Help.Delete"));
                return;
            case "list":
                commandSender.sendMessage(s + "list: " + getLangString("SinkCity.City.Help.List"));
                return;
            case "join":
                commandSender.sendMessage(s + "join <" + getLangString("Help.City") + ">: " + getLangString("SinkCity.City.Help.Join"));
                return;
            case "leave":
                commandSender.sendMessage(s + "leave <" + getLangString("Help.City") + ">: " + getLangString("SinkCity.City.Help.Leave"));
                return;
            case "here":
                commandSender.sendMessage(s + "here: " + getLangString("SinkCity.City.Help.Here"));
                return;
            case "info":
                commandSender.sendMessage(s + "info <" + getLangString("Help.City") + ">: " + getLangString("SinkCity.City.Help.Info"));
                return;
            case "set":
                commandSender.sendMessage(s + "set <" + getLangString("Help.City") + ">:" + getLangString("SinkCity.City.Help.Set"));
                return;
            default:
                commandSender.sendMessage(prefix + getLangString("Help.CommandNotAvailable"));
                return;
        }
    }

    @Override
    protected boolean onExecute(CommandSender sender, String label, String[] args) throws ParseException {
        if (args.length < 1) {
            return false;
        }

        switch (args[0]) {
            case "create":
                if (args.length == 1) {
                    sendHelp(sender, label, "create");
                    return true;
                }
                String cityName = null;
                UUID cityId = UUID.randomUUID();
                Player mayor = null;
                Location spawn = null;
                if (args.length == 2) {
                    if (!(sender instanceof Player)) {
                        sendHelp(sender, label, args[0]);
                        return true;
                    }
                    cityName = args[1];
                    mayor = ((Player) sender);
                }
                if (args.length > 2) {
                    cityName = args[1];
                    mayor = BukkitUtil.getPlayer(args[2]);
                    if (mayor == null) {
                        sender.sendMessage(Utils.format(getLangString("Help.UserNotFound"), args[2]));
                        return true;
                    }
                }
                if (mayor == null)
                    return true;
                spawn = mayor.getLocation();

                SinkCity.getDatabaseHandler().storeCity(cityName, cityId, mayor, spawn);
                return true;
            default:
                return false;
        }
    }
}
