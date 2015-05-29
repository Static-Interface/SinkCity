package de.static_interface.sinkcity;

import java.io.File;

import de.static_interface.sinklibrary.api.configuration.Configuration;

public class LanguageConfiguration extends Configuration {

    private static LanguageConfiguration INSTANCE;

    protected static void init(SinkCity sinkCity) {
        INSTANCE = new LanguageConfiguration(sinkCity, true);
    }

    private LanguageConfiguration(final SinkCity sinkCity, boolean init) {
        super(new File(sinkCity.getDataFolder().getAbsolutePath() + File.separator + "sinkcity.lang"), init);
    }

    public static String getString(String path) {
        return String.valueOf(INSTANCE.get(path));
    }

    public static LanguageConfiguration getInstance() {
        return INSTANCE;
    }

    @Override
    public void addDefaults() {
        this.addDefault("Database.UnsupportedType", "&4The requested database type is not supported.");
        this.addDefault("Database.ConnectionFailed", "&4Could not connect to the database. Deactivating.");
        this.addDefault("SinkCity.City.DeletionStarted", "Started deleting the city {0}.");
        this.addDefault("SinkCity.City.DeletionFinished", "Finished deleting the city {0} after {1} seconds.");
        this.addDefault("SinkCity.City.List.Start", "Available cities:");
        this.addDefault("SinkCity.City.List.Format", "{0}[{1} residents] ({2})");
        this.addDefault("SinkCity.City.List.NoCities", "There are no cities available.");
    }
}
