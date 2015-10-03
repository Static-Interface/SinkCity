package de.static_interface.sinkcity;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import de.static_interface.sinkcity.commands.CityCommand;
import de.static_interface.sinkcity.commands.PlotCommand;
import de.static_interface.sinkcity.commands.ResidentCommand;
import de.static_interface.sinkcity.commands.SinkCityCommand;
import de.static_interface.sinkcity.commands.WarCommand;
import de.static_interface.sinkcity.database.DatabaseHandler;
import de.static_interface.sinkcity.database.SQLDatabaseHandler;
import de.static_interface.sinkcity.database.rows.CityChunkRow;
import de.static_interface.sinkcity.database.rows.CityChunkSettingRow;
import de.static_interface.sinkcity.database.rows.CityChunk_CityResident_Row;
import de.static_interface.sinkcity.database.rows.CityRankPermissionRow;
import de.static_interface.sinkcity.database.rows.CityRankRow;
import de.static_interface.sinkcity.database.rows.CityResident_CityRank_Row;
import de.static_interface.sinkcity.database.rows.CityResident_City_Row;
import de.static_interface.sinkcity.database.rows.CityRow;
import de.static_interface.sinkcity.database.rows.CitySettingRow;
import de.static_interface.sinkcity.database.rows.City_CityRank_Row;
import de.static_interface.sinkcity.database.tables.CityChunkSettingTable;
import de.static_interface.sinkcity.database.tables.CityChunkTable;
import de.static_interface.sinkcity.database.tables.CityChunk_CityResident_Table;
import de.static_interface.sinkcity.database.tables.CityRankPermissionTable;
import de.static_interface.sinkcity.database.tables.CityRankTable;
import de.static_interface.sinkcity.database.tables.CityResident_CityRank_Table;
import de.static_interface.sinkcity.database.tables.CityResident_City_Table;
import de.static_interface.sinkcity.database.tables.CitySettingTable;
import de.static_interface.sinkcity.database.tables.CityTable;
import de.static_interface.sinkcity.database.tables.City_CityRank_Table;
import de.static_interface.sinkcity.listeners.BlockListener;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.impl.database.H2Database;
import de.static_interface.sinklibrary.database.impl.database.MySqlDatabase;

public class SinkCity extends JavaPlugin {

    private static SinkCity instance;

    public static final String prefix = ChatColor.AQUA + "[" + ChatColor.DARK_GREEN + "SinkCity" + ChatColor.AQUA + "] " + ChatColor.RESET;

    private static DatabaseHandler databaseHandler;

    private Map<Class<? extends Row>, AbstractTable<? extends Row>> tables;

    private CityCommand cityCommand;
    private SinkCityCommand sinkCityCommand;
    private PlotCommand plotCommand;
    private ResidentCommand residentCommand;
    private WarCommand warCommand;

    private BlockListener blockBreakListener = new BlockListener();

    public static SinkCity getInstance() {
        return instance;
    }

    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Make sure the data folder exists.
        if (!this.getDataFolder().exists()) {
            try {
                Files.createDirectories(this.getDataFolder().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Load configurations
        LanguageConfiguration.init(this);
        DatabaseConfiguration.init(this);

        // Prepares database connections
        prepareDatabases();

        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        this.cityCommand = new CityCommand(this);
        this.sinkCityCommand = new SinkCityCommand(this);
        this.plotCommand = new PlotCommand(this);
        this.residentCommand = new ResidentCommand(this);
        this.warCommand = new WarCommand(this);

        SinkLibrary.getInstance().registerCommand("city", this.cityCommand);
        SinkLibrary.getInstance().registerCommand("sinkcity", this.sinkCityCommand);
        SinkLibrary.getInstance().registerCommand("plot", this.plotCommand);
        SinkLibrary.getInstance().registerCommand("resident", this.residentCommand);
        SinkLibrary.getInstance().registerCommand("war", this.warCommand);
    }

    @SuppressWarnings("deprecation")
    public void prepareDatabases() {
        this.tables = new HashMap<Class<? extends Row>, AbstractTable<? extends Row>>();
        // Prepares the connections to the database.
        // Actual database operations are handled using DatabaseHandlers.
        Database database;
        if (DatabaseConfiguration.getInstance().getString("DATABASE.TYPE").equalsIgnoreCase("mysql")) {
            database = new MySqlDatabase(DatabaseConfiguration.getInstance(), this);
        } else {
            database = new H2Database(DatabaseConfiguration.getInstance(), this);
        }
        try {
            database.connect();

            // Entity types
            AbstractTable<CityRow> cityTable = new CityTable(database);
            AbstractTable<CityChunkRow> cityChunkTable = new CityChunkTable(database);
            AbstractTable<CityChunkSettingRow> cityChunkSettingTable = new CityChunkSettingTable(database);
            AbstractTable<CityResident_City_Row> cityResidentCityTable = new CityResident_City_Table(database);
            AbstractTable<CityRankRow> cityRankTable = new CityRankTable(database);
            AbstractTable<CityRankPermissionRow> cityRankPermissionTable = new CityRankPermissionTable(database);
            AbstractTable<CitySettingRow> citySettingTable = new CitySettingTable(database);

            // relation tables
            AbstractTable<City_CityRank_Row> cityRankCityTable = new City_CityRank_Table(database);
            AbstractTable<CityResident_CityRank_Row> cityResidentCityRankTable = new CityResident_CityRank_Table(database);
            AbstractTable<CityChunk_CityResident_Row> cityChunkCityResidentTable = new CityChunk_CityResident_Table(database);

            cityTable.create();
            cityChunkTable.create();
            cityChunkSettingTable.create();
            citySettingTable.create();
            cityResidentCityTable.create();
            cityRankTable.create();
            cityRankPermissionTable.create();
            citySettingTable.create();

            cityRankCityTable.create();
            cityResidentCityRankTable.create();
            cityChunkCityResidentTable.create();

            this.tables.put(CityRow.class, cityTable);
            this.tables.put(CityChunkRow.class, cityChunkTable);
            this.tables.put(CityChunkSettingRow.class, cityChunkSettingTable);
            this.tables.put(CitySettingRow.class, citySettingTable);
            this.tables.put(CityResident_City_Row.class, cityResidentCityTable);
            this.tables.put(CityRankRow.class, cityRankTable);
            this.tables.put(CityRankPermissionRow.class, cityRankPermissionTable);
            this.tables.put(CitySettingRow.class, citySettingTable);

            this.tables.put(City_CityRank_Row.class, cityRankCityTable);
            this.tables.put(CityResident_CityRank_Row.class, cityResidentCityRankTable);
            this.tables.put(CityChunk_CityResident_Row.class, cityChunkCityResidentTable);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not establish a connection to the database. Disabling", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        databaseHandler = new SQLDatabaseHandler();
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this.blockBreakListener, this);
    }

    public AbstractTable<? extends Row> getTableForClass(Class<? extends Row> tableRowClass) {
        return this.tables.get(tableRowClass);
    }
}
