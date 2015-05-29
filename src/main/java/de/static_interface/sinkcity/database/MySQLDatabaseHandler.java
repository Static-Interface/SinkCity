package de.static_interface.sinkcity.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import de.static_interface.sinkcity.DatabaseConfiguration;
import de.static_interface.sinkcity.LanguageConfiguration;
import de.static_interface.sinkcity.SinkCity;

public class MySQLDatabaseHandler extends DatabaseHandler {

    private Connection connection;

    private volatile Map<String, City> previouslyLoadedCities = new HashMap<String, City>();

    public MySQLDatabaseHandler() {
        String user, pass, database, server;
        user = DatabaseConfiguration.getInstance().get("MYSQL.USER").toString();
        pass = DatabaseConfiguration.getInstance().get("MYSQL.PASS").toString();
        database = DatabaseConfiguration.getInstance().get("MYSQL.DB").toString();
        server = DatabaseConfiguration.getInstance().get("MYSQL.SERVER").toString();
        String connectionString = String.format("jdbc:mysql://%s/%s?user=%s", server, database, user);
        if (!pass.isEmpty()) {
            connectionString += "&?password=" + pass;
        }
        try {
            this.connection = DriverManager.getConnection(connectionString);
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS cities(cityname TEXT, cityid VARCHAR(36), worldid VARCHAR(36))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS citydata(cityid VARCHAR(36), mayorid VARCHAR(36), citysettings BIGINT, spawnX INT, spawnY INT, spawnZ INT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS chunks(coordx INT, coordz INT, ownerid VARCHAR(36), cityid VARCHAR(36), homechunk TINYINT)");
        } catch (SQLException e) {
            this.connection = null;
            Bukkit.getLogger().log(Level.SEVERE, LanguageConfiguration.getString("Database.ConnectionFailed"), e);
            Bukkit.getPluginManager().disablePlugin(SinkCity.instance);
        }
    }

    @Override
    public boolean storeCity(City city) {
        try {
            // null is not allowed as a city name.
            if (city.getCityName().equalsIgnoreCase("null"))
                return false;

            ResultSet set = this.connection.getMetaData().getTables(null, null, city.getCityId().toString(), null);
            if (!set.next()) {
                // City doesn't exist.
                PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO cities(?, ?, ?)");
                preparedStatement.setString(1, city.getCityName());
                preparedStatement.setString(2, city.getCityId().toString());
                preparedStatement.setString(3, city.getWorld().getUID().toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                preparedStatement = this.connection.prepareStatement("INSERT INTO citydata(?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, city.getCityId().toString());
                preparedStatement.setString(2, city.getMayor().toString());
                preparedStatement.setInt(3, city.getCitySettings());
                preparedStatement.setInt(4, city.getSpawnX());
                preparedStatement.setInt(5, city.getSpawnY());
                preparedStatement.setInt(6, city.getSpawnZ());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                for (Chunk chunk : city.getChunks()) {
                    preparedStatement = this.connection.prepareStatement("INSERT INTO chunks(?, ?, ?, ?, ?)");
                    preparedStatement.setInt(1, chunk.getX());
                    preparedStatement.setInt(2, chunk.getZ());
                    preparedStatement.setNull(3, Types.VARCHAR);
                    preparedStatement.setString(4, city.getCityId().toString());
                    preparedStatement.setInt(5, (city.getHomeChunk() == chunk ? 1 : 0));
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
                String sttmnt = String.format("CREATE TABLE `%s`(residentid VARCHAR(36), assistant TINYINT)", city.getCityId());
                Statement statement = this.connection.createStatement();
                statement.execute(sttmnt);
                statement.close();
                sttmnt = String.format("INSERT INTO `%s` VALUES(?, ?)", city.getCityId().toString());
                for (UUID uuid : city.getResidents()) {
                    preparedStatement = this.connection.prepareStatement(sttmnt);
                    preparedStatement.setString(1, uuid.toString());
                    preparedStatement.setInt(2, city.getAssistants().contains(uuid) ? 1 : 0);
                    preparedStatement.close();
                }
            } else {
                // City exists.

            }
        } catch (SQLException e) {

        }
        return false;
    }

    @Override
    public synchronized City loadCity(String cityName) {
        for (Entry<String, City> loadedCities : this.previouslyLoadedCities.entrySet()) {
            if (loadedCities.getKey().equalsIgnoreCase(cityName))
                return loadedCities.getValue();
        }
        try {
            // Get city id and world.
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM cities WHERE cityname=?");
            preparedStatement.setString(1, cityName);
            ResultSet set = preparedStatement.executeQuery();
            City city;
            World world;
            UUID cityId = null, worldId = null;
            if (set.next()) {
                cityId = UUID.fromString(set.getString("cityid"));
                worldId = UUID.fromString(set.getString("worldid"));
                city = new City(cityName, cityId);
                world = Bukkit.getWorld(worldId);
                city.setWorld(world);
            } else {
                return null;
            }
            set.close();
            preparedStatement.close();
            // Get the mayor's id, the city settings and the spawn position.
            preparedStatement = this.connection.prepareStatement("SELECT * FROM citydata WHERE cityid=?");
            preparedStatement.setString(1, cityId.toString());
            set = preparedStatement.executeQuery();
            UUID mayorid;
            int citysettings, spawnX, spawnY, spawnZ;
            if (set.next()) {
                mayorid = UUID.fromString(set.getString("mayorid"));
                citysettings = set.getInt("citysettings");
                spawnX = set.getInt("spawnX");
                spawnY = set.getInt("spawnY");
                spawnZ = set.getInt("spawnZ");
                city.setMayor(mayorid);
                city.setCitySettings(citysettings);
                city.setSpawn(spawnX, spawnY, spawnZ);
            } else {
                return null;
            }
            set.close();
            preparedStatement = this.connection.prepareStatement("SELECT * FROM chunks WHERE cityid=?");
            preparedStatement.setString(1, cityId.toString());
            set = preparedStatement.executeQuery();

            // Get chunks, owned chunks, the home chunk
            List<Chunk> chunks = new ArrayList<Chunk>();
            Map<Chunk, UUID> ownedChunks = new HashMap<Chunk, UUID>();
            Chunk homeChunk = null;
            Chunk chunk;
            int chunkX, chunkZ;
            String ownerIdString;
            UUID ownerId;
            boolean isHomeChunk = false;
            while (set.next()) {
                chunkX = set.getInt("coordx");
                chunkZ = set.getInt("coordz");
                chunk = Bukkit.getWorld(worldId).getChunkAt(chunkX, chunkZ);
                ownerIdString = set.getString("ownerid");
                if (ownerIdString != null) {
                    ownerId = UUID.fromString(ownerIdString);
                    ownedChunks.put(chunk, ownerId);
                }
                isHomeChunk = set.getBoolean("homechunk");
                chunks.add(chunk);
                if (isHomeChunk)
                    homeChunk = chunk;
            }
            set.close();
            preparedStatement.close();
            city.setChunks(chunks);
            city.setHomeChunk((homeChunk == null ? chunks.get(0) : homeChunk));
            city.setOwnedChunks(ownedChunks);
            String sttmnt = String.format("SELECT * FROM `%s`", city.getCityId().toString());
            preparedStatement = this.connection.prepareStatement(sttmnt);

            // Get residents & assistants.
            set = preparedStatement.executeQuery();
            List<UUID> residents = new ArrayList<UUID>();
            List<UUID> assistants = new ArrayList<UUID>();
            UUID residentid;
            boolean assistant;
            while (set.next()) {
                residentid = UUID.fromString(set.getString("residentid"));
                assistant = set.getBoolean("assistant");
                residents.add(residentid);
                if (assistant)
                    assistants.add(residentid);
            }
            city.setResidents(residents);
            city.setAssistants(assistants);
            return city;
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "The plugin could not load the city " + cityName, e);
        }
        return null;
    }

    @Override
    public List<City> getAvailableCities() {
        List<City> cities = new ArrayList<City>();
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM cities");
            ResultSet resultSet = preparedStatement.executeQuery();
            String cityName;
            City city;
            while (resultSet.next()) {
                cityName = resultSet.getString("cityname");
                city = loadCity(cityName);
                cities.add(city);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load cities!", e);
        }
        return cities;
    }

    @Override
    public Map<Chunk, UUID> getBoughtChunks(String cityName) {
        Map<Chunk, UUID> boughtChunks = new HashMap<Chunk, UUID>();
        // cities(cityname TEXT, cityid VARCHAR(36), worldid VARCHAR(36))
        // chunks(coordx INT, coordz INT, ownerid VARCHAR(36), cityid VARCHAR(36), homechunk TINYINT)
        UUID worldId, cityId;
        World world;
        try {
            // Get the world and the city id
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM cities WHERE cityname=?");
            statement.setString(1, cityName);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                worldId = UUID.fromString(set.getString("worldid"));
                world = Bukkit.getWorld(worldId);
                cityId = UUID.fromString(set.getString("cityid"));
            } else {
                return boughtChunks;
            }
            set.close();
            statement.close();
            // Get the chunks in the city.
            statement = this.connection.prepareStatement("SELECT coordx,coordz,ownerid FROM chunks WHERE cityid=? AND ownerid NOT NULL");
            statement.setString(1, cityId.toString());
            set = statement.executeQuery();
            Chunk chunk;
            int coordx, coordz;
            String ownerIdString;
            UUID ownerId;
            while (set.next()) {
                coordx = set.getInt("coordx");
                coordz = set.getInt("coordz");
                ownerIdString = set.getString("ownerid");
                ownerId = UUID.fromString(ownerIdString);
                chunk = world.getChunkAt(coordx, coordz);
                boughtChunks.put(chunk, ownerId);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load data from database.", e);
        }
        return boughtChunks;
    }

    /*
     * cities(cityname TEXT, cityid VARCHAR(36), worldid VARCHAR(36))
     * citydata(cityid VARCHAR(36), mayorid VARCHAR(36), citysettings BIGINT, spawnX INT, spawnY INT, spawnZ INT)
     * chunks(coordx INT, coordz INT, ownerid VARCHAR(36), cityid VARCHAR(36), homechunk TINYINT
     */

    @Override
    public synchronized boolean dropCity(City city) {

        return false;
    }

    @Override
    public synchronized boolean renameCity(String oldCityName, String newCityName) {
        // TODO
        return false;
    }

}
