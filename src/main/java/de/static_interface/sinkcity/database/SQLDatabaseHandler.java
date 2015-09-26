package de.static_interface.sinkcity.database;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SQLDatabaseHandler extends DatabaseHandler {

    @Override
    public City storeCity(String cityName, UUID cityId, Player mayor, Location spawn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateCity(City city) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public City loadCity(String cityName, boolean reloadFromDatabase) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int dropCity(City city) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<City> getAvailableCities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Chunk, UUID> getBoughtChunks(String cityName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean renameCity(City city, String newCityName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int addPlayerToCity(Player player, City city) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int removePlayerFromCity(Player player, City city) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public City cityAt(Chunk chunk) {
        // TODO Auto-generated method stub
        return null;
    }

}
