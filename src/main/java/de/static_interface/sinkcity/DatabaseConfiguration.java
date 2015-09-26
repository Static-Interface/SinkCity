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

import de.static_interface.sinklibrary.database.DatabaseConnectionInfo;

public class DatabaseConfiguration extends Properties implements DatabaseConnectionInfo {

    private static DatabaseConfiguration INSTANCE;
    private static File file;

    protected static void init(SinkCity sinkCity) {
        INSTANCE = new DatabaseConfiguration(sinkCity);
    }

    private DatabaseConfiguration(SinkCity sinkCity) {
        file = new File(sinkCity.getDataFolder().getAbsolutePath(), "database.cfg.properties");
        if ((file.exists())) {
            // Load from file.
            try {
                Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                load(reader);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not read default configuration: an IO-Exception caused SinkCity to fail. Deactivating.", e);
                Bukkit.getPluginManager().disablePlugin(sinkCity);
            }
        }
        if ((!file.exists())) {
            // Write new file
            addDefaults();
            try {
                Files.createFile(file.toPath());
            } catch (IOException e1) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not create default configuration: an IO-Exception caused SinkCity to fail. Deactivating", e1);
                Bukkit.getPluginManager().disablePlugin(sinkCity);
            }
            try (Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                store(writer, "Please make this configuration match your needs.");
                writer.close();
                Bukkit.getLogger().log(Level.SEVERE, "A default configurarion for databases has been written. Please make it match your needs first.");
                Bukkit.getPluginManager().disablePlugin(sinkCity);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not create default configuration: an IO-Exception caused SinkCity to fail. Deactivating", e);
                Bukkit.getPluginManager().disablePlugin(sinkCity);
            }
        }
    }

    @Override
    public String getUsername() {
        return getString("MYSQL.USER");
    }

    @Override
    public String getTablePrefix() {
        return "SinkCity_";
    }

    @Override
    public int getPort() {
        return Integer.parseInt(get("MYSQL.PORT").toString());
    }

    @Override
    public String getPassword() {
        return getString("MYSQL.PASS");
    }

    @Override
    public String getDatabaseName() {
        return getString("MYSQL.DB");
    }

    @Override
    public String getAddress() {
        return getString("MYSQL.SERVER");
    }

    public static DatabaseConfiguration getInstance() {
        return INSTANCE;
    }

    public String getString(String key) {
        return getInstance().get(key).toString();
    }

    private void addDefaults() {
        this.setProperty("DATABASE.TYPE", "MYSQL");
        this.setProperty("MYSQL.USER", "root");
        this.setProperty("MYSQL.PASS", "");
        this.setProperty("MYSQL.DB", "SinkCity");
        this.setProperty("MYSQL.SERVER", "127.0.0.1");
        this.setProperty("MYSQL.PORT", "3306");
    }
}
