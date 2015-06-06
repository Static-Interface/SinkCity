package de.static_interface.sinkcity;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.static_interface.sinklibrary.util.StringUtil;

public class LanguageConfiguration extends Properties {

    private static LanguageConfiguration INSTANCE;
    private static Properties DEFAULTS;
    private static File file;

    protected static void init(SinkCity sinkCity) {
        DEFAULTS = new Properties();
        addDefaults(DEFAULTS);
        INSTANCE = new LanguageConfiguration(sinkCity);
    }

    private LanguageConfiguration(final SinkCity sinkCity) {
        file = new File(sinkCity.getDataFolder(), "sinkcity.lang.properties");
        if (file.exists()) {
            // Load configuration.
            try {
                Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                load(reader);
                reader.close();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not load language configuration. Using default.", e);
                addDefaults(this);
            }
        } else {
            // Write default configuration.
            try {
                file.getParentFile().mkdirs();
                addDefaults(this);
                file.createNewFile();
                Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
                store(writer, "Default language configuration.");
                writer.close();
                Bukkit.getLogger().log(Level.SEVERE, "Default configuration written. Please make it match your needs. The default configuration is used now.");
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "An IO exception caused the writing process of the default configuration to fail. Using defaults now.", e);
            }
        }
    }

    public static String getString(String path) {
        return StringUtil.format(String.valueOf(INSTANCE.get(path) == null ? DEFAULTS.get(path) : INSTANCE.get(path)));
    }

    public static LanguageConfiguration getInstance() {
        return INSTANCE;
    }

    private static void addDefaults(Properties properties) {
        properties.setProperty("Database.UnsupportedType", "&4The requested database type is not supported.");
        properties.setProperty("Database.ConnectionFailed", "&4Could not connect to the database. Deactivating.");
        properties.setProperty("Database.InternalError", "An internal error occurred with the database. Please ask an administrator for help.");
        properties.setProperty("Help.Command", "Command");
        properties.setProperty("Help.City", "City");
        properties.setProperty("Help.Mayor", "Mayor");
        properties.setProperty("Help.Residents", "Residents");
        properties.setProperty("Help.AndMore", "and more");
        properties.setProperty("Help.AvailableCommands", "&bAvailable commands:");
        properties.setProperty("Help.CommandNotAvailable", "&4This command is not available.");

        properties.setProperty("SinkCity.City.Settings.FIRE", "Fire");
        properties.setProperty("SinkCity.City.Settings.MOBS", "Mobs");
        properties.setProperty("SinkCity.City.Settings.MONSTERS", "Monsters");
        properties.setProperty("SinkCity.City.Settings.TNT_BUILD", "TNT building");
        properties.setProperty("SinkCity.City.Settings.TNT_EXPLODE", "TNT explosions");
        properties.setProperty("SinkCity.City.Settings.EXPLOSION", "Other explosions");
        properties.setProperty("SinkCity.City.Settings.OPEN_CITY", "Open city");

        properties.setProperty("SinkCity.City.NotFound", "&cCity not found.");
        properties.setProperty("SinkCity.City.Help.Create", "Found a city at the chunk the mayor is currently in.");
        properties.setProperty("SinkCity.City.Help.Delete", "Delete a city and release all of its chunks to the wilderness.");
        properties.setProperty("SinkCity.City.Help.List", "Lists all available cities.");
        properties.setProperty("SinkCity.City.Help.Join", "Join a city.");
        properties.setProperty("SinkCity.City.Help.Leave", "Leave a city. Mayors can not leave without declaring a new mayor");
        properties.setProperty("SinkCity.City.Help.Here", "Tells you which city is situated at that chunk.");
        properties.setProperty("SinkCity.City.Help.Info", "Gives you information about the city.");

        properties.setProperty("SinkCity.City.JoinSucceed", "&aYou have successfully joined {0}.");
        properties.setProperty("SinkCity.City.ResidentJoined", "&a{0} has joined the city {1}!");
        properties.setProperty("SinkCity.City.JoinFailed", "&aJoining the city {0} failed. Please ask an administrator for advise");
        properties.setProperty("SinkCity.City.LeaveSucceed", "&aYou have successfully left {0}.");
        properties.setProperty("SinkCity.City.ResidentLeft", "&a{0} has left the city {1}!");
        properties.setProperty("SinkCity.City.LeaveFailed", "&cLeaving the city {0} failed.");

        properties.setProperty("SinkCity.City.CreationSucceed", "&bSuccessfully founded the city {0}.");
        properties.setProperty("SinkCity.City.CreationFailed.NameTaken", "&cThat name is already taken.");
        properties.setProperty("SinkCity.City.CreationFailed.CityExistsAtPlace", "&cThere is already a city here.");
        properties.setProperty("SinkCity.City.CreationFailed.NameEqualsNull", "&aA city may not be named \"null\"!");
        properties.setProperty("SinkCity.City.CreationFailed.InternalException", "&aCould not found city because of an internal error. Please ask an administrator for help.");

        properties.setProperty("SinkCity.City.DeletionFailed", "&cInternal error. Please ask the administrator for advise!");
        properties.setProperty("SinkCity.City.DeletionSucceed", "{0} was successfully deleted.");

        properties.setProperty("SinkCity.City.List.Start", "Available cities:");
        properties.setProperty("SinkCity.City.List.Format", "{0}[{1} residents] ({2})");
        properties.setProperty("SinkCity.City.List.NoCities", "There are no cities available.");
        properties.setProperty("SinkCity.City.NoCityHere", "There is no city here.");
        properties.setProperty("SinkCity.City.Here", "The city {0} is here.");
    }
}
