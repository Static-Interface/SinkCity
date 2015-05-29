package de.static_interface.sinkcity.database;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;

public abstract class DatabaseHandler {

    /**
     * Stores the {@link City} instance to the database in a way, that every
     * detail about the city can be retrieved from it.
     * 
     * @param city
     *            The city to store.
     * @return <code>true</code> if the city was stored without problems, else
     *         <code>false</code>.
     */
    public abstract boolean storeCity(City city);

    /**
     * Builds a {@link City} instance from the information stored in the
     * database or returns it from a {@link Map} if the city has already been
     * loaded earlier.
     * 
     * @param cityName
     *            The name of the city to load.
     * @return A {@link City} instance or <code>null</code> if an exception
     *         occurred or the city doesn't exist.
     */
    public abstract City loadCity(String cityName);

    /**
     * Drops a city from the database, deleting it completely.
     * 
     * @param city
     *            The {@link City} instance to delete.
     * @return <code>true</code> if the city got dropped successfully, else
     *         false. If <code>false</code> is returned, it doesn't necessarily
     *         mean that the city is still in the database completely or in
     *         pieces.
     */
    public abstract boolean dropCity(City city);

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
     * Renames a city in the database. This happens only if the old city name
     * matches {@link String#equalsIgnoreCase(String)} with the parameter.
     * 
     * @param oldCityName
     * @param newCityName
     * @return <code>true</code> if the renaming progress ended successfully,
     *         else false.
     */
    public abstract boolean renameCity(String oldCityName, String newCityName);
}
