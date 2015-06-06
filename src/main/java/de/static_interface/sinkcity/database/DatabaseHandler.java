package de.static_interface.sinkcity.database;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public abstract class DatabaseHandler {

    /**
     * Stores the {@link City} instance to the database in a way, that every
     * detail about the city can be retrieved from it.
     * 
     * @param city
     *            The city to store.
     * @return
     */
    public abstract ResultCode storeCity(City city);

    /**
     * Updates a {@link City} in the database.
     * 
     * @param city
     * @return
     */
    public abstract ResultCode updateCity(City city);

    /**
     * Builds a {@link City} instance from the information stored in the
     * database or returns it from a {@link Map} if the city has already been
     * loaded earlier.
     * 
     * @param cityName
     *            The name of the city to load.
     * @param reloadFromDatabase
     *            If this is <code>true</code>, the cache will be updated using
     *            the data from the database which will revert any changes which
     *            were not synchronized to the database yet.
     * @return A {@link City} instance or <code>null</code> if an exception
     *         occurred or the city doesn't exist.
     */
    public abstract City loadCity(String cityName, boolean reloadFromDatabase);

    /**
     * Drops a city from the database, deleting it completely.
     * 
     * @param city
     *            The {@link City} instance to delete.
     * @return
     */
    public abstract ResultCode dropCity(City city);

    /**
     * @return A list of all cities that are stored in the database. Never
     *         <code>null</code>.
     */
    public abstract List<City> getAvailableCities();

    /**
     * @param cityName
     * @return A map of chunks that have been bought in the {@link City},
     *         storing the {@link Chunk} and the {@link UUID} of the owner.
     */
    public abstract Map<Chunk, UUID> getBoughtChunks(String cityName);

    /**
     * Renames a city in the database.
     * 
     * @param city
     * @param newCityName
     * @return
     */
    public abstract ResultCode renameCity(City city, String newCityName);

    /**
     * Adds a player's {@link UUID} to a city to make him a resident of that
     * {@link City}.
     * 
     * @param player
     * @param city
     * @return
     */
    public abstract ResultCode addPlayerToCity(Player player, City city);

    /**
     * Looks up if there are any cities situated in that chunk and returns it if
     * there are any.
     * 
     * @param chunk
     * @return
     */
    public abstract City cityAt(Chunk chunk);
}
