package de.static_interface.sinkcity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import de.static_interface.sinkcity.commands.CityCommand;
import de.static_interface.sinkcity.commands.SinkCityCommand;
import de.static_interface.sinkcity.database.DatabaseHandler;
import de.static_interface.sinkcity.database.MySQLDatabaseHandler;
import de.static_interface.sinklibrary.SinkLibrary;

public class SinkCity extends JavaPlugin {

    public static SinkCity instance;

    public static final String prefix = ChatColor.AQUA + "[" + ChatColor.DARK_GREEN + "SinkCity" + ChatColor.AQUA + "] " + ChatColor.RESET;

    private static DatabaseHandler databaseHandler;

    private CityCommand cityCommand = new CityCommand(this);
    private SinkCityCommand sinkCityCommand = new SinkCityCommand(this);

    @Override
    public void onEnable() {
        instance = this;
        LanguageConfiguration.init(this);
        DatabaseConfiguration.init(this);
        if (DatabaseConfiguration.getInstance().get("DATABASE.TYPE").toString().equalsIgnoreCase("mysql")) {
            databaseHandler = new MySQLDatabaseHandler();
        } else {
            databaseHandler = null;
            Bukkit.getLogger().severe(LanguageConfiguration.getString("Database.UnsupportedType"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
        SinkLibrary.getInstance().registerCommand("city", this.cityCommand);
        SinkLibrary.getInstance().registerCommand("sinkcity", this.sinkCityCommand);
    }

    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }
}
