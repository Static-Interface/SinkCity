package de.static_interface.sinkcity.database;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class DatabaseHandler {

    /**
     * Creates a new {@link City} instance and stores it to the database.
     * 
     * @param city
     *            The city to store.
     * @return 0 if everything worked out fine<br>
     *         -1 if the city name is "null" or <code>null</code><br>
     *         -2 if the cityid or the cityname is already taken<br>
     *         -3 if there is a city at the current chunk<br>
     */
    public abstract City storeCity(String cityName, UUID cityId, Player mayor, Location spawn);

    /**
     * Updates a {@link City} in the database.
     * 
     * @param city
     * @return 0 if the city has been updated<br>
     *         -1 if the city name is "null" or <code>null</code><br>
     *         -2 if the city doesn't exist<br>
     */
    public abstract int updateCity(City city);

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
     * @return 0 if the city has been dropped<br>
     *         -1 if the city doesn't exist<br>
     */
    public abstract int dropCity(City city);

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
     * @return <code>true</code> if the city has been renamed successfully<br>
     *         <code>false</code> if an internal exception occurred
     */
    public abstract boolean renameCity(City city, String newCityName);

    /**
     * Adds a player's {@link UUID} to a city to make him a resident of that
     * {@link City}.
     * 
     * @param player
     * @param city
     * @return 0 if the player has been added successfully<br>
     *         -1 if the city doesn't exist<br>
     */
    public abstract int addPlayerToCity(Player player, City city);

    /**
     * Removes a player's {@link UUID} from a city to un-resident him from that
     * {@link City}.
     * 
     * @param player
     * @param city
     * @return 0 if the player has been removed successfully<br>
     *         -1 if the city doesn't exist<br>
     */
    public abstract int removePlayerFromCity(Player player, City city);

    /**
     * Looks up if there are any cities situated in that chunk and returns it if
     * there are any.
     * 
     * @param chunk
     * @return
     */
    public abstract City cityAt(Chunk chunk);
}
