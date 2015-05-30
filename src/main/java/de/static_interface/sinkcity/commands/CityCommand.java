package de.static_interface.sinkcity.commands;

import static de.static_interface.sinkcity.LanguageConfiguration.getString;
import static de.static_interface.sinkcity.SinkCity.prefix;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.cli.ParseException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.static_interface.sinkcity.SinkCity;
import de.static_interface.sinkcity.database.City;
import de.static_interface.sinklibrary.api.command.SinkCommand;
import de.static_interface.sinklibrary.api.sender.IrcCommandSender;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import de.static_interface.sinklibrary.util.BukkitUtil;
import de.static_interface.sinklibrary.util.StringUtil;

public class CityCommand extends SinkCommand {

    public CityCommand(SinkCity plugin) {
        super(plugin);
    }

    @Override
    protected boolean onExecute(CommandSender commandSender, String label, String[] args) throws ParseException {
        if (args.length < 1) {
            sendHelp(commandSender, label, null);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                sendHelp(commandSender, label, args.length == 1 ? null : args[1].toLowerCase());
                return true;
            case "create":
                if (args.length < 2) {
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.TooFewArguments"));
                    return true;
                }

                UUID cityId = UUID.randomUUID();
                String cityName;
                Location spawn;
                Player mayor;
                City city;

                if (args.length == 2) {
                    // The new mayor is the sender.
                    if (commandSender instanceof ConsoleCommandSender) {
                        commandSender.sendMessage(prefix + LanguageConfiguration.m("General.ConsoleNotAvailable"));
                        return true;
                    }
                    cityName = args[1];
                    mayor = BukkitUtil.getPlayer(commandSender.getName());
                    spawn = mayor.getLocation();
                    city = new City(cityName, cityId, spawn, mayor);
                } else {
                    // The sender is a different player than the mayor.
                    cityName = args[1];
                    mayor = BukkitUtil.getPlayer(args[2]);
                    if (mayor == null) {
                        commandSender.sendMessage(prefix + StringUtil.format(LanguageConfiguration.m("General.UserNotFound", args[3])));
                        return true;
                    }
                    spawn = mayor.getLocation();
                    city = new City(cityName, cityId, spawn, mayor);
                }

                if (SinkCity.getDatabaseHandler().storeCity(city)) {
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.CreationSucceed"), city.getCityName()));
                } else {
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.CreationFailed"), city.getCityName()));
                }
                return true;
            case "delete":
                if (args.length < 2) {
                    commandSender.sendMessage(prefix + StringUtil.format(LanguageConfiguration.m("General.TooFewArguments")));
                    return true;
                }

                cityName = args[1];
                city = SinkCity.getDatabaseHandler().loadCity(cityName);
                if (city.getCityName().equalsIgnoreCase(cityName)) {
                    city = SinkCity.getDatabaseHandler().loadCity(cityName);
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.DeletionStarted"), cityName));
                    boolean result = SinkCity.getDatabaseHandler().dropCity(city);
                    if (result) {
                        commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.DeletionFinished"), cityName));
                    } else {
                        commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.DeletionFailed"), city.getCityName()));
                    }
                    return true;
                } else {
                    commandSender.sendMessage(prefix + getString("SinkCity.City.NotFound"));
                    return true;
                }
            case "list":
                List<City> cities = SinkCity.getDatabaseHandler().getAvailableCities();
                commandSender.sendMessage(prefix + getString("SinkCity.City.List.Start"));
                if (cities.isEmpty()) {
                    commandSender.sendMessage(prefix + getString("SinkCity.City.List.NoCities"));
                    return true;
                } else {
                    for (City c : cities) {
                        commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.List.Format"), c.getCityName(), c.getResidents().size(), c.getWorld().getName()));
                    }
                    return true;
                }
            case "here":
                if ((commandSender instanceof ConsoleCommandSender) || (commandSender instanceof IrcCommandSender)) {
                    commandSender.sendMessage(prefix + StringUtil.format(LanguageConfiguration.m("General.ConsoleNotAvailable")));
                    return true;
                }
                Player player = ((Player) commandSender);
                city = SinkCity.getDatabaseHandler().cityAt(player.getLocation().getChunk());
                if (city == null) {
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.NoCityHere")));
                } else {
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.Here"), city.getCityName()));
                }
                return true;
            case "join":
                if (args.length <= 1) {
                    sendHelp(commandSender, label, "join");
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(StringUtil.format(LanguageConfiguration.m("General.ConsoleNotAvailable")));
                    return true;
                }
                player = ((Player) commandSender);
                cityName = args[1];
                city = SinkCity.getDatabaseHandler().loadCity(cityName);

                if (SinkCity.getDatabaseHandler().addPlayerToCity(player, city)) {
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.JoinSucceed"), city.getCityName()));
                    Player target;
                    String displayName = BukkitUtil.getNameByUniqueId(player.getUniqueId());
                    for (UUID uuid : city.getResidents()) {
                        target = Bukkit.getPlayer(uuid);
                        if (target == null)
                            continue;
                        target.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.ResidentJoined"), displayName, city.getCityName()));
                    }
                    return true;
                } else {
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.JoinFailed"), city.getCityName()));
                    return true;
                }
            default:
                sendHelp(commandSender, label, null);
                return true;
        }
    }

    private static void sendHelp(CommandSender commandSender, String label, @Nullable String subCommand) {
        String s = StringUtil.format(prefix + "/" + label + " ");
        if (subCommand == null) {
            commandSender.sendMessage(prefix + StringUtil.format(getString("Help.AvailableCommands")));
            commandSender.sendMessage(s + "create");
            commandSender.sendMessage(s + "delete");
            commandSender.sendMessage(s + "list");
            commandSender.sendMessage(s + "join");
            commandSender.sendMessage(s + "here");
            commandSender.sendMessage(s + "leave");
            commandSender.sendMessage(s + String.format("help <%s>", getString("Help.Command")));
        } else {
            switch (subCommand) {
                case "create":
                    commandSender.sendMessage(s + "create <cityname> [mayor]: " + getString("SinkCity.City.Help.Create"));
                    return;
                case "delete":
                    commandSender.sendMessage(s + "delete <city>: " + getString("SinkCity.City.Help.Delete"));
                    return;
                case "list":
                    commandSender.sendMessage(s + "list: " + getString("SinkCity.City.Help.List"));
                    return;
                case "join":
                    commandSender.sendMessage(s + "join <cityname>: " + getString("SinkCity.City.Help.Join"));
                    return;
                case "leave":
                    commandSender.sendMessage(s + "leave <cityname>: " + getString("SinkCity.City.Help.Leave"));
                    return;
                case "here":
                    commandSender.sendMessage(s + "here: " + getString("SinkCity.City.Help.Here"));
                    return;
                default:
                    commandSender.sendMessage(prefix + getString("Help.CommandNotAvailable"));
                    return;
            }
        }
    }
}
