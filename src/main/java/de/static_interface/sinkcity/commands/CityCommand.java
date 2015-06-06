package de.static_interface.sinkcity.commands;

import static de.static_interface.sinkcity.LanguageConfiguration.getString;
import static de.static_interface.sinkcity.SinkCity.prefix;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.cli.ParseException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.static_interface.sinkcity.CitySettings;
import de.static_interface.sinkcity.SinkCity;
import de.static_interface.sinkcity.database.City;
import de.static_interface.sinkcity.database.ResultCode;
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
                    if ((commandSender instanceof ConsoleCommandSender) || (commandSender instanceof IrcCommandSender)) {
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
                        commandSender.sendMessage(prefix + LanguageConfiguration.m("General.UserNotFound", args[3]));
                        return true;
                    }
                    spawn = mayor.getLocation();
                    city = new City(cityName, cityId, spawn, mayor);
                }

                ResultCode code = SinkCity.getDatabaseHandler().storeCity(city);
                switch (code) {
                    case CITY_STORED:
                        commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.CreationSucceed"), city.getCityName()));
                        return true;
                    case CITY_EXISTS_HERE:
                        commandSender.sendMessage(prefix + getString("SinkCity.City.CreationFailed.CityExistsAtPlace"));
                        return true;
                    case CITY_NAME_EXISTS:
                        commandSender.sendMessage(prefix + getString("SinkCity.City.CreationFailed.NameTaken"));
                        return true;
                    case CITY_NAME_EQUALS_NULL:
                        commandSender.sendMessage(prefix + getString("SinkCity.City.CreationFailed.NameEqualsNull"));
                        return true;
                    default:
                        commandSender.sendMessage(prefix + getString("SinkCity.City.CreationFailed.InternalException"));
                        return true;
                }
            case "delete":
                if (args.length < 2) {
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.TooFewArguments"));
                    return true;
                }

                cityName = args[1];
                city = SinkCity.getDatabaseHandler().loadCity(cityName, false);
                if (city.getCityName().equalsIgnoreCase(cityName)) {
                    city = SinkCity.getDatabaseHandler().loadCity(cityName, false);
                    code = SinkCity.getDatabaseHandler().dropCity(city);
                    switch (code) {
                        case CITY_DOESNT_EXIST:
                            commandSender.sendMessage(prefix + getString("SinkCity.City.NotFound"));
                            return true;
                        case CITY_DROPPED:
                            commandSender.sendMessage(prefix + getString("SinkCity.City.DeletionFailed"));
                            return true;
                        default:
                            commandSender.sendMessage(prefix + getString("Database.InternalError"));
                            return true;
                    }
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
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.ConsoleNotAvailable"));
                    return true;
                }
                Player player = ((Player) commandSender);
                city = SinkCity.getDatabaseHandler().cityAt(player.getLocation().getChunk());
                if (city == null) {
                    commandSender.sendMessage(prefix + getString("SinkCity.City.NoCityHere"));
                } else {
                    commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.Here"), city.getCityName()));
                }
                return true;
            case "join":
                if (args.length <= 1) {
                    sendHelp(commandSender, label, "join");
                    return true;
                }
                if ((commandSender instanceof ConsoleCommandSender) || (commandSender instanceof IrcCommandSender)) {
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.ConsoleNotAvailable"));
                    return true;
                }
                player = ((Player) commandSender);
                cityName = args[1];
                city = SinkCity.getDatabaseHandler().loadCity(cityName, false);
                code = SinkCity.getDatabaseHandler().addPlayerToCity(player, city);
                switch (code) {
                    case PLAYER_ADDED:
                        commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.JoinSucceed"), city.getCityName()));
                        String displayName = BukkitUtil.getNameByUniqueId(player.getUniqueId());
                        Player target;
                        for (UUID uuid : city.getResidents()) {
                            target = Bukkit.getPlayer(uuid);
                            if ((target == null) || (target == player))
                                continue;
                            target.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.ResidentJoined"), displayName, city.getCityName()));
                        }
                        return true;
                    default:
                        commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.JoinFailed"), city.getCityName()));
                        return true;
                }
            case "leave":
                if ((commandSender instanceof ConsoleCommandSender) || (commandSender instanceof IrcCommandSender)) {
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.ConsoleNotAvailable"));
                    return true;
                }
                player = ((Player) commandSender);
                cityName = args[1];
                city = SinkCity.getDatabaseHandler().loadCity(cityName, false);
                for (Entry<Chunk, UUID> ownedChunks : city.getOwnedChunks().entrySet()) {
                    if (ownedChunks.getValue().equals(player.getUniqueId())) {
                        // Give the chunks back to the city.
                        city.getOwnedChunks().remove(ownedChunks.getKey());
                    }
                }

                code = SinkCity.getDatabaseHandler().updateCity(city);
                switch (code) {
                    case CITY_UPDATED:
                        commandSender.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.LeaveSucceed"), city.getCityName()));
                        Player target;
                        String displayName = BukkitUtil.getNameByUniqueId(player.getUniqueId());
                        for (UUID uuid : city.getResidents()) {
                            target = Bukkit.getPlayer(uuid);
                            if ((target == null) || (target == commandSender))
                                continue;
                            target.sendMessage(prefix + StringUtil.format(getString("SinkCity.City.ResidentLeft"), displayName, city.getCityName()));
                        }
                        SinkCity.getDatabaseHandler().updateCity(city);
                        return true;
                    case CITY_NAME_EQUALS_NULL:
                    case CITY_DOESNT_EXIST:
                        commandSender.sendMessage(prefix + getString("SinkCity.City.NotFound"));
                        return true;
                    default:
                        commandSender.sendMessage(prefix + getString("Database.InternalError"));
                        return true;
                }
            case "info":
                if (args.length <= 1) {
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.TooFewArguments"));
                    return true;
                }

                cityName = args[1];
                city = SinkCity.getDatabaseHandler().loadCity(cityName, true);
                if (city == null) {
                    commandSender.sendMessage(prefix + getString("SinkCity.City.NotFound"));
                    return true;
                } else {
                    StringBuilder activatedSettings = new StringBuilder();
                    for (CitySettings settings : CitySettings.values()) {
                        if (CitySettings.settingActivated(settings, city.getCitySettings())) {
                            activatedSettings.append(ChatColor.GREEN);
                        } else {
                            activatedSettings.append(ChatColor.RED);
                        }
                        activatedSettings.append(getString("SinkCity.City.Settings." + settings.name()));
                        activatedSettings.append(ChatColor.RESET);
                        activatedSettings.append(", ");
                        if (settings == CitySettings.TNT_EXPLODE)
                            activatedSettings.append("\n");
                    }
                    String activatedSettingsString = activatedSettings.toString();
                    String spawnString = "Spawn: " + city.getSpawnX() + "/" + city.getSpawnY() + "/" + city.getSpawnZ();
                    StringBuilder stringBuilder = new StringBuilder();
                    int residentsCounted = 0;
                    for (UUID uuid : city.getResidents()) {
                        if (residentsCounted >= 40) {
                            stringBuilder.append(getString("Help.AndMore"));
                        }
                        stringBuilder.append(BukkitUtil.getNameByUniqueId(uuid));
                        if (city.getMayor().equals(uuid)) {
                            stringBuilder.append(ChatColor.DARK_RED + " [M]" + ChatColor.RESET);
                        }
                        if (city.getAssistants().contains(uuid)) {
                            stringBuilder.append(ChatColor.DARK_GREEN + " [A]" + ChatColor.RESET);
                        }
                        if (residentsCounted != city.getResidents().size())
                            stringBuilder.append(", ");
                        residentsCounted++;
                    }
                    String residentsString = stringBuilder.toString();
                    commandSender.sendMessage(ChatColor.GREEN + String.format("------------- %s (%s) -------------", city.getCityName(), city.getWorld().getName()));
                    commandSender.sendMessage(activatedSettingsString);
                    commandSender.sendMessage(spawnString);
                    commandSender.sendMessage(getString("Help.Residents"));
                    commandSender.sendMessage(residentsString);
                    return true;
                }
            case "spawn":
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.ConsoleNotAvailable"));
                    return true;
                }
                if (args.length <= 1) {
                    commandSender.sendMessage(prefix + LanguageConfiguration.m("General.TooFewArguments"));
                    return true;
                }

                String targetCity = args[1];
                city = SinkCity.getDatabaseHandler().loadCity(targetCity, false);
                if (city == null) {
                    commandSender.sendMessage(prefix + getString("SinkCity.City.NotFound"));
                    return true;
                } else {
                    Location loc = new Location(city.getWorld(), city.getSpawnX(), city.getSpawnY(), city.getSpawnZ());
                    ((Player) commandSender).teleport(loc, TeleportCause.COMMAND);
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
            commandSender.sendMessage(s + "info");
            commandSender.sendMessage(s + String.format("help <%s>", getString("Help.Command")));
        } else {
            switch (subCommand) {
                case "create":
                    commandSender.sendMessage(s + "create <" + getString("Help.City") + "> [" + getString("Help.Mayor") + "]: " + getString("SinkCity.City.Help.Create"));
                    return;
                case "delete":
                    commandSender.sendMessage(s + "delete <" + getString("Help.City") + ">: " + getString("SinkCity.City.Help.Delete"));
                    return;
                case "list":
                    commandSender.sendMessage(s + "list: " + getString("SinkCity.City.Help.List"));
                    return;
                case "join":
                    commandSender.sendMessage(s + "join <" + getString("Help.City") + ">: " + getString("SinkCity.City.Help.Join"));
                    return;
                case "leave":
                    commandSender.sendMessage(s + "leave <" + getString("Help.City") + ">: " + getString("SinkCity.City.Help.Leave"));
                    return;
                case "here":
                    commandSender.sendMessage(s + "here: " + getString("SinkCity.City.Help.Here"));
                    return;
                case "info":
                    commandSender.sendMessage(s + "info <" + getString("Help.City") + ">: " + getString("SinkCity.City.Help.Info"));
                default:
                    commandSender.sendMessage(prefix + getString("Help.CommandNotAvailable"));
                    return;
            }
        }
    }
}
