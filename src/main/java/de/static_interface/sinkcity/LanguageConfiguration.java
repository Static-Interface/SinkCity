package de.static_interface.sinkcity;

import java.io.File;

import de.static_interface.sinklibrary.api.configuration.Configuration;
import de.static_interface.sinklibrary.util.StringUtil;

public class LanguageConfiguration extends Configuration {

    private static LanguageConfiguration INSTANCE;
    private static Configuration DEFAULTS;

    protected static void init(SinkCity sinkCity) {
        DEFAULTS = new LanguageConfiguration(sinkCity, false);
        DEFAULTS.addDefaults();
        INSTANCE = new LanguageConfiguration(sinkCity);
    }

    private LanguageConfiguration(final SinkCity sinkCity) {
        this(sinkCity, true);
    }

    private LanguageConfiguration(final SinkCity sinkCity, boolean init) {
        super(new File(sinkCity.getDataFolder(), "sinkcity.lang.yml"), init);
    }

    public static String getString(String path) {
        return StringUtil.format(String.valueOf(INSTANCE.get(path) == null ? DEFAULTS.get(path) : INSTANCE.get(path)));
    }

    public static LanguageConfiguration getInstance() {
        return INSTANCE;
    }

    @Override
    public void addDefaults() {
        this.set("Database.UnsupportedType", "&4The requested database type is not supported.");
        this.set("Database.ConnectionFailed", "&4Could not connect to the database. Deactivating.");
        this.set("Database.InternalError", "An internal error occurred with the database. Please ask an administrator for help.");
        this.set("Help.Command", "Command");
        this.set("Help.City", "City");
        this.set("Help.Mayor", "Mayor");
        this.set("Help.Residents", "Residents");
        this.set("Help.AndMore", "and more");
        this.set("Help.AvailableCommands", "&bAvailable commands:");
        this.set("Help.CommandNotAvailable", "&4This command is not available.");

        this.set("SinkCity.City.Settings.FIRE", "Fire");
        this.set("SinkCity.City.Settings.MOBS", "Mobs");
        this.set("SinkCity.City.Settings.MONSTERS", "Monsters");
        this.set("SinkCity.City.Settings.TNT_BUILD", "TNT building");
        this.set("SinkCity.City.Settings.TNT_EXPLODE", "TNT explosions");
        this.set("SinkCity.City.Settings.EXPLOSION", "Other explosions");
        this.set("SinkCity.City.Settings.OPEN_CITY", "Open city");

        this.set("SinkCity.City.NotFound", "&cCity not found.");
        this.set("SinkCity.City.Help.Create", "Found a city at the chunk the mayor is currently in.");
        this.set("SinkCity.City.Help.Delete", "Delete a city and release all of its chunks to the wilderness.");
        this.set("SinkCity.City.Help.List", "Lists all available cities.");
        this.set("SinkCity.City.Help.Join", "Join a city.");
        this.set("SinkCity.City.Help.Leave", "Leave a city. Mayors can not leave without declaring a new mayor");
        this.set("SinkCity.City.Help.Here", "Tells you which city is situated at that chunk.");
        this.set("SinkCity.City.Help.Info", "Gives you information about the city.");

        this.set("SinkCity.City.JoinSucceed", "&aYou have successfully joined {0}.");
        this.set("SinkCity.City.ResidentJoined", "&a{0} has joined the city {1}!");
        this.set("SinkCity.City.JoinFailed", "&aJoining the city {0} failed. Please ask an administrator for advise");
        this.set("SinkCity.City.LeaveSucceed", "&aYou have successfully left {0}.");
        this.set("SinkCity.City.ResidentLeft", "&a{0} has left the city {1}!");
        this.set("SinkCity.City.LeaveFailed", "&cLeaving the city {0} failed.");

        this.set("SinkCity.City.CreationSucceed", "&bSuccessfully founded the city {0}.");
        this.set("SinkCity.City.CreationFailed.NameTaken", "&cThat name is already taken.");
        this.set("SinkCity.City.CreationFailed.CityExistsAtPlace", "&cThere is already a city here.");
        this.set("SinkCity.City.CreationFailed.NameEqualsNull", "&aA city may not be named \"null\"!");
        this.set("SinkCity.City.CreationFailed.InternalException", "&aCould not found city because of an internal error. Please ask an administrator for help.");

        this.set("SinkCity.City.DeletionFailed", "&cInternal error. Please ask the administrator for advise!");
        this.set("SinkCity.City.DeletionSucceed", "{0} was successfully deleted.");

        this.set("SinkCity.City.List.Start", "Available cities:");
        this.set("SinkCity.City.List.Format", "{0}[{1} residents] ({2})");
        this.set("SinkCity.City.List.NoCities", "There are no cities available.");
        this.set("SinkCity.City.NoCityHere", "There is no city here.");
        this.set("SinkCity.City.Here", "The city {0} is here.");

    }
}