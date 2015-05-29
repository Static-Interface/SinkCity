package de.static_interface.sinkcity.commands;

import static de.static_interface.sinkcity.LanguageConfiguration.getString;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.cli.ParseException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.static_interface.sinkcity.SinkCity;
import de.static_interface.sinkcity.database.City;
import de.static_interface.sinklibrary.api.command.SinkCommand;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import de.static_interface.sinklibrary.util.BukkitUtil;
import de.static_interface.sinklibrary.util.StringUtil;

public class CityCommand extends SinkCommand {

    public CityCommand(SinkCity plugin) {
        super(plugin);
    }

    @Override
    protected boolean onExecute(CommandSender sender, String label, String[] args) throws ParseException {
        if (args.length < 1) {
            // Send usage.
            return false;
        }

        Bukkit.getLogger().log(Level.SEVERE, this.getPermission());
        Bukkit.getLogger().log(Level.SEVERE, String.valueOf(sender.hasPermission(this.getPermission())));

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfiguration.m("General.TooFewArguments")));
                    return true;
                }

                UUID cityId = UUID.randomUUID();
                String cityName;
                Location spawn;
                Player mayor;
                City city;

                if (args.length == 3) {
                    // The new mayor is the sender.
                    if (sender instanceof CommandSender) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfiguration.m("General.ConsoleNotAvailable")));
                        return true;
                    }
                    cityName = args[0];
                    mayor = BukkitUtil.getPlayer(sender.getName());
                    spawn = mayor.getLocation();
                    city = new City(cityName, cityId, spawn, mayor);
                } else {
                    // The sender is a different player than the mayor.
                    cityName = args[0];
                    mayor = BukkitUtil.getPlayer(args[3]);
                    if (mayor == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfiguration.m("General.UserNotFound", args[3])));
                        return true;
                    }
                    spawn = mayor.getLocation();
                    city = new City(cityName, cityId, spawn, mayor);
                }

                SinkCity.databaseHandler.storeCity(city);
                return true;
            case "delete":
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfiguration.m("General.TooFewArguments")));
                    return true;
                }

                cityName = args[1];
                List<City> cities = SinkCity.databaseHandler.getAvailableCities();
                for (City c : cities) {
                    if (c.getCityName().equalsIgnoreCase(cityName)) {
                        city = SinkCity.databaseHandler.loadCity(cityName);
                        sender.sendMessage(SinkCity.prefix + StringUtil.format(getString("SinkCity.City.DeletionStarted"), cityName));
                        long start = System.currentTimeMillis();
                        SinkCity.databaseHandler.dropCity(city);
                        long end = System.currentTimeMillis();
                        long neededTime = (end - start) / 1000;
                        sender.sendMessage(SinkCity.prefix + StringUtil.format(getString("SinkCity.City.DeletionFinished"), cityName, neededTime));
                        return true;
                    }
                }
                return false;
            case "list":
                cities = SinkCity.databaseHandler.getAvailableCities();
                sender.sendMessage(SinkCity.prefix + getString("SinkCity.City.List.Start"));
                if (cities.isEmpty()) {
                    sender.sendMessage(SinkCity.prefix + getString("SinkCity.City.List.NoCities"));
                    return true;
                } else {
                    for (City c : cities) {
                        sender.sendMessage(SinkCity.prefix + StringUtil.format(getString("SinkCity.City.List.Format"), c.getCityName(), c.getResidents().size(), c.getWorld().getName()));
                    }
                    return true;
                }
            default:
                return false;
        }
    }
}
