package de.static_interface.sinkcity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.static_interface.sinklibrary.util.StringUtil;

public class LanguageConfiguration extends Properties {

    private static LanguageConfiguration INSTANCE;
    private static File file;

    protected static void init(SinkCity sinkCity) {
        INSTANCE = new LanguageConfiguration(sinkCity);
    }

    private LanguageConfiguration(final SinkCity sinkCity) {
        file = new File(sinkCity.getDataFolder(), "sinkcity.lang.xml");
        if (file.exists()) {
            // Load configuration.
            try {
                InputStream reader = new FileInputStream(file);
                loadFromXML(reader);
                reader.close();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not load language configuration. Using default.", e);
                addDefaults();
            }
        } else {
            // Write default configuration.
            try {
                file.getParentFile().mkdirs();
                addDefaults();
                file.createNewFile();
                OutputStream writer = new FileOutputStream(file);
                storeToXML(writer, "Default language configuration.", StandardCharsets.UTF_8.name());
                writer.close();
                Bukkit.getLogger().log(Level.SEVERE, "Default configuration written. Please make it match your needs. The default configuration is used now.");
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "An IO exception caused the writing process of the default configuration to fail. Using defaults now.", e);
            }
        }
    }

    public static String getString(String path) {
        return StringUtil.format(String.valueOf(INSTANCE.get(path) == null ? getDefault(path) : INSTANCE.get(path)));
    }

    public static LanguageConfiguration getInstance() {
        return INSTANCE;
    }

    private void addDefaults() {
        this.setProperty("Database.UnsupportedType", "&4The requested database type is not supported.");
        this.setProperty("Database.ConnectionFailed", "&4Could not connect to the database. Deactivating.");
        this.setProperty("Help.Command", "Command");
        this.setProperty("Help.AvailableCommands", "&bAvailable commands:");
        this.setProperty("Help.CommandNotAvailable", "&4This command is not available.");

        this.setProperty("SinkCity.City.NotFound", "&4City not found.");
        this.setProperty("SinkCity.City.Help.Create", "Create a city at the chunk the mayor is currently in.");
        this.setProperty("SinkCity.City.Help.Delete", "Delete a city and release all of its chunks to the wilderness.");
        this.setProperty("SinkCity.City.Help.List", "Lists all available cities.");
        this.setProperty("SinkCity.City.Help.Join", "Join a city.");
        this.setProperty("SinkCity.City.Help.Leave", "Leave a city. Mayors can not leave without declaring a new mayor");
        this.setProperty("SinkCity.City.Help.Here", "Tells you which city is situated at that chunk.");

        this.setProperty("SinkCity.City.JoinSucceed", "&aYou have successfully joined {0}.");
        this.setProperty("SinkCity.City.ResidentJoined", "&a{0} has joined the city {1}!");
        this.setProperty("SinkCity.City.JoinFailed", "&aJoining the city {0} failed.");
        this.setProperty("SinkCity.City.CreationSucceed", "&bSuccessfully created the city {0}.");
        this.setProperty("SinkCity.City.CreationFailed", "&cCould not create the city {0}. Please ask the administrator for advise.");
        this.setProperty("SinkCity.City.DeletionFailed", "&cCould not delete the city {0} successfully. Please ask the administrator for advise!");
        this.setProperty("SinkCity.City.DeletionStarted", "Started deleting the city {0}.");
        this.setProperty("SinkCity.City.DeletionFinished", "Finished deleting the city {0} after {1} milliseconds.");
        this.setProperty("SinkCity.City.List.Start", "Available cities:");
        this.setProperty("SinkCity.City.List.Format", "{0}[{1} residents] ({2})");
        this.setProperty("SinkCity.City.List.NoCities", "There are no cities available.");
        this.setProperty("SinkCity.City.NoCityHere", "There is no city here.");
        this.setProperty("SinkCity.City.Here", "The city {0} is here.");
    }

    private static String getDefault(String key) {
        switch (key) {
            case "Database.UnsupportedType":
                return "&4The requested database type is not supported.";
            case "Database.ConnectionFailed":
                return "&4Could not connect to the database. Deactivating.";
            case "Help.Command":
                return "Command";
            case "Help.AvailableCommands":
                return "&bAvailable commands:";
            case "Help.CommandNotAvailable":
                return "&4This command is not available.";
            case "SinkCity.City.NotFound":
                return "&4City not found.";
            case "SinkCity.City.Help.Create":
                return "Create a city at the chunk the mayor is currently in.";
            case "SinkCity.City.Help.Delete":
                return "Delete a city and release all of its chunks to the wilderness.";
            case "SinkCity.City.Help.List":
                return "Lists all available cities.";
            case "SinkCity.City.Help.Join":
                return "Join a city.";
            case "SinkCity.City.Help.Leave":
                return "Leave a city. Mayors can not leave without declaring a new mayor";
            case "SinkCity.City.Help.Here":
                return "Tells you which city is situated at that chunk.";
            case "SinkCity.City.Here":
                return "The city {0} is here.";
            case "SinkCity.City.NoCityHere":
                return "There is no city here.";
            case "SinkCity.City.JoinSucceed":
                return "&aYou have successfully joined {0}.";
            case "SinkCity.City.ResidentJoined":
                return "&a{0} has joined the city {1}!";
            case "SinkCity.City.JoinFailed":
                return "&aJoining the city {0} failed.";
            case "SinkCity.City.CreationSucceed":
                return "&bSuccessfully created the city {0}.";
            case "SinkCity.City.CreationFailed":
                return "&cCould not create the city {0}. Please ask the administrator for advise.";
            case "SinkCity.City.DeletionFailed":
                return "&cCould not delete the city {0} successfully. Please ask the administrator for advise!";
            case "SinkCity.City.DeletionStarted":
                return "Started deleting the city {0}.";
            case "SinkCity.City.DeletionFinished":
                return "Finished deleting the city {0} after {1} milliseconds.";
            case "SinkCity.City.List.Start":
                return "Available cities:";
            case "SinkCity.City.List.Format":
                return "{0}[{1} residents] ({2})";
            case "SinkCity.City.List.NoCities":
                return "There are no cities available.";
            default:
                return String.valueOf(null);
        }
    }
}
