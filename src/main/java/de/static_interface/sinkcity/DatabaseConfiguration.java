package de.static_interface.sinkcity;

import java.io.File;

import de.static_interface.sinklibrary.api.configuration.Configuration;

public class DatabaseConfiguration extends Configuration {

    private static DatabaseConfiguration INSTANCE;

    protected static void init(SinkCity sinkCity) {
        INSTANCE = new DatabaseConfiguration(sinkCity, true);
    }

    private DatabaseConfiguration(SinkCity sinkCity, boolean init) {
        super(new File(sinkCity.getDataFolder().getAbsolutePath() + File.separator + "database.cfg"), init);
    }

    public static DatabaseConfiguration getInstance() {
        return INSTANCE;
    }

    @Override
    public void addDefaults() {
        this.addDefault("DATABASE.TYPE", "MYSQL");
        this.addDefault("MYSQL.USER", "root");
        this.addDefault("MYSQL.PASS", "");
        this.addDefault("MYSQL.DB", "SinkCity");
        this.addDefault("MYSQL.SERVER", "127.0.0.1");
    }

}
