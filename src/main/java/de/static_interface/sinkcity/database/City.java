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

public class City {

    private World world;
    private List<Chunk> chunks;
    private Chunk homeChunk;
    private int spawnX, spawnY, spawnZ;
    private List<UUID> residents;
    private List<UUID> assistants;
    private UUID mayor;
    private UUID cityId;
    private String cityName;
    private int citySettings;
    private Map<Chunk, UUID> ownedChunks;

    protected City(String cityName, UUID cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public City(String cityName, UUID cityId, Location spawn, Player mayor) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.world = spawn.getWorld();
        this.homeChunk = spawn.getChunk();
        this.chunks = new ArrayList<Chunk>();
        this.chunks.add(this.getHomeChunk());
        this.spawnX = spawn.getBlockX();
        this.spawnY = spawn.getBlockY();
        this.spawnZ = spawn.getBlockZ();
        this.ownedChunks = new HashMap<Chunk, UUID>();
        this.citySettings = 0;
        this.residents = new ArrayList<UUID>();
        this.residents.add(mayor.getUniqueId());
        this.assistants = new ArrayList<UUID>();
        this.assistants.add(mayor.getUniqueId());
        this.mayor = mayor.getUniqueId();
    }

    protected void setCitySettings(int citySettings) {
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

    protected void setHomeChunk(Chunk homeChunk) {
        this.homeChunk = homeChunk;
    }

    protected void setChunks(List<Chunk> chunks) {
        this.chunks = chunks;
    }

    protected void setResidents(List<UUID> residents) {
        this.residents = residents;
    }

    protected void setAssistants(List<UUID> assistants) {
        this.assistants = assistants;
    }

    protected void setMayor(UUID mayor) {
        this.mayor = mayor;
    }

    protected void setOwnedChunks(Map<Chunk, UUID> ownedChunks) {
        this.ownedChunks = ownedChunks;
    }

    protected void setCityName(String cityName) {
        this.cityName = cityName;
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
     * @return The {@link UUID}s of the assistants.
     */
    public List<UUID> getAssistants() {
        return this.assistants;
    }

    /**
     * @return The {@link UUID} of the mayor of this city.
     */
    public UUID getMayor() {
        return this.mayor;
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
    public int getCitySettings() {
        return this.citySettings;
    }

    /**
     * @return The x-coordinate of the feet on the spawn point.
     */
    public int getSpawnX() {
        return this.spawnX;
    }

    /**
     * @return The y-coordinate of the feet on the spawn point.
     */
    public int getSpawnY() {
        return this.spawnY;
    }

    /**
     * @return The z-coordinate of the feet on the spawn point.
     */
    public int getSpawnZ() {
        return this.spawnZ;
    }

    /**
     * @return The home chunk of the city.
     */
    public Chunk getHomeChunk() {
        return this.homeChunk;
    }

    public Map<Chunk, UUID> getOwnedChunks() {
        return this.ownedChunks;
    }
}
