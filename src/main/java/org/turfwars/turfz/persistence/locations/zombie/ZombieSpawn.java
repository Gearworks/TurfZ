package org.turfwars.turfz.persistence.locations.zombie;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.turfwars.turfz.utilities.LocationUtil;

import java.util.ArrayList;
import java.util.List;

public class ZombieSpawn {

    private final Location location;
    private final int radius;

    /**
     * Constructor will load all the locations from the database
     */
    public ZombieSpawn (final Location location, int radius){
        this.location = location;
        this.radius = radius;
    }

    /**
     *
     * @return the location of the spawn point
     */
    public Location getLocation (){
        return location;
    }

    /**
     *
     * @return the radius zombies can spawn in
     */
    public int getRaidus (){
        return radius;
    }

    /**
     *
     * @return all the players inside the radius of the spawn point
     */
    public List<Player> getPlayersInsideRegion (){
        List<Player> players = new ArrayList<Player> ();

        for (final Player player : Bukkit.getOnlinePlayers ()){
            if (LocationUtil.inRadius (location, player.getLocation (), radius)){
                players.add (player);
            }
        }

        return players;
    }
}
