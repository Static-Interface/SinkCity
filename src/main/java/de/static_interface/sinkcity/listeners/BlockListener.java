package de.static_interface.sinkcity.listeners;

import static de.static_interface.sinkcity.LanguageConfiguration.getLangString;
import static de.static_interface.sinkcity.SinkCity.prefix;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.static_interface.sinkcity.SinkCity;
import de.static_interface.sinkcity.database.City;

public class BlockListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        City city = SinkCity.getDatabaseHandler().cityAt(location.getChunk());
        if (city == null) {
            return;
        } else {
            if (city.getResidents().contains(event.getPlayer().getUniqueId())) {
                return;
            } else {
                event.setExpToDrop(0);
                event.setCancelled(true);
                event.getPlayer().sendMessage(prefix + getLangString("SinkCity.City.DestroyingDisallowed"));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Location location = event.getBlock().getLocation();
        City city = SinkCity.getDatabaseHandler().cityAt(location.getChunk());
        if (city == null) {
            return;
        } else {
            if (city.getResidents().contains(event.getPlayer().getUniqueId())) {
                return;
            } else {
                event.setBuild(false);
                event.setCancelled(true);
                event.getPlayer().sendMessage(prefix + getLangString("SinkCity.City.BuildingDisallowed"));
            }
        }
    }
}
