package de.static_interface.sinkcity.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.static_interface.sinkcity.CityPermissions;
import de.static_interface.sinkcity.CitySettings;
import de.static_interface.sinkcity.SinkCity;
import de.static_interface.sinkcity.api.Plugin;

public class City {

    /**
     * The world where the city's homechunk is in.
     */
    private World world;
    /**
     * A list of {@link Chunk}s this city owns.
     */
    private List<Chunk> chunks;
    /**
     * The approximate spawn position's coordinates.
     */
    private double spawnX, spawnY, spawnZ;
    /**
     * A list of {@link UUID}s of all the residents.
     */
    private List<UUID> residents;
    /**
     * The unique ID of this city.
     */
    private UUID cityId;
    /**
     * The city name. May not be unique, may change.
     */
    private String cityName;
    /**
     * A map of all the city settings (i.e. the settings that are different from
     * the default values)
     */
    private Map<String, Object> citySettings;
    /**
     * A {@link Map} of {@link Chunk}s that have owners in the city (i.e.
     * someone bought it)
     */
    private Map<Chunk, UUID> ownedChunks;

    /**
     * A map of the residents with their respective ranks (i.e. the ID of their
     * ranks). Will not be filled until the city has been stored once.
     */
    private Map<UUID, Integer> residentRanks;

    /**
     * A map of the ranks associated with the granted permissions per rank.
     */
    private Map<Integer, List<CityPermissions>> rankPermissions;

    public City(String cityName, UUID cityId, Location spawn, Player mayor) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.world = spawn.getWorld();
        this.chunks = new ArrayList<Chunk>();
        this.chunks.add(spawn.getChunk());
        this.spawnX = spawn.getBlockX();
        this.spawnY = spawn.getBlockY();
        this.spawnZ = spawn.getBlockZ();
        this.ownedChunks = new HashMap<Chunk, UUID>();
        this.citySettings = new HashMap<String, Object>();
        this.residents = new ArrayList<UUID>();
        this.residents.add(mayor.getUniqueId());
        this.residentRanks = new HashMap<UUID, Integer>();
        this.rankPermissions = new HashMap<Integer, List<CityPermissions>>();
    }

    protected void setCitySettings(Map<String, Object> citySettings) {
        this.citySettings = citySettings;
    }

    protected void setWorld(World world) {
        this.world = world;
    }

    protected void setSpawn(int spawnX, int spawnY, int spawnZ) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
    }

    protected void setChunks(List<Chunk> chunks) {
        this.chunks = chunks;
    }

    protected void setResidents(List<UUID> residents) {
        this.residents = residents;
    }

    protected void setOwnedChunks(Map<Chunk, UUID> ownedChunks) {
        this.ownedChunks = ownedChunks;
    }

    protected void setCityName(String cityName) {
        this.cityName = cityName;
    }

    protected void setUserRank(UUID playerId, Integer rank) {
        this.residentRanks.put(playerId, rank);
    }

    protected void setRankPermission(Integer rank, List<CityPermissions> permissions) {
        this.rankPermissions.put(rank, permissions);
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * @return all chunks that are covered by this city including, the home
     *         chunk.
     */
    public List<Chunk> getChunks() {
        return this.chunks;
    }

    /**
     * @return The {@link UUID}s of all residents of this city, including the
     *         assistants and the mayor.
     */
    public List<UUID> getResidents() {
        return this.residents;
    }

    /**
     * @return The {@link UUID} of this city.
     */
    public UUID getCityId() {
        return this.cityId;
    }

    /**
     * @return The name of this city.
     */
    public String getCityName() {
        return this.cityName;
    }

    /**
     * @return The city settings as an <code>int</code>.
     */
    public Map<String, Object> getCitySettings() {
        return this.citySettings;
    }

    /**
     * @return The spawn's location
     */
    public Location getSpawn() {
        return new Location(this.world, this.spawnX, this.spawnY, this.spawnZ);
    }

    /**
     * @return The chunks in the city that someone bought, with the UUID of the
     *         owner.
     */
    public Map<Chunk, UUID> getOwnedChunks() {
        return this.ownedChunks;
    }

    /**
     * @return The ID's of the ranks associated with the granted permissions as
     *         a map.
     */
    public Map<Integer, List<CityPermissions>> getRankPermissions() {
        return this.rankPermissions;
    }

    /**
     * @return The residents associated with their respective ranks' ID.
     */
    public Map<UUID, Integer> getResidentRanks() {
        return this.residentRanks;
    }

    /**
     * Updates a <b>core</b> setting.
     * 
     * @param setting
     *            The {@link CitySettings} to change.
     * @param value
     *            The new value.
     */
    public void updateSetting(CitySettings setting, Object value) {
        this.citySettings.put(SinkCity.class.getName() + "." + setting.name(), value);
    }

    /**
     * Updates a plugin setting.
     * 
     * @param pluginClass
     *            The main class of the plugin.
     * @param settingName
     *            The name of the setting.
     * @param value
     *            The value of the setting.
     */
    public void updateCustomSetting(Class<? extends Plugin> pluginClass, String settingName, Object value) {
        this.citySettings.put(pluginClass.getName() + "." + settingName, value);
    }
}
