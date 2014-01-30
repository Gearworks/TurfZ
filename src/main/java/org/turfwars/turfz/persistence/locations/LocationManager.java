package org.turfwars.turfz.persistence.locations;

import org.bukkit.Location;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.locations.spawns.PlayerSpawn;
import org.turfwars.turfz.persistence.locations.spawns.ZombieSpawn;
import org.turfwars.turfz.utilities.LocationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationManager {

    private final List<ZombieSpawn> zombieSpawns = new ArrayList<ZombieSpawn> ();
    private final List<PlayerSpawn> playerSpawns = new ArrayList<PlayerSpawn>();

    public LocationManager (){
        loadZombieSpawns ();
        loadPlayerSpawns ();
    }

    /**
     * Will load all the spawns spawn points saved into the flat file for spawns
     */
    private void loadZombieSpawns () {
        final List<String> zombieList = TurfZ.getConfigRegistry ().getSpawnConfig ().getStringList ("zombies");

        for (final String zombieData : zombieList){
            zombieSpawns.add (LocationUtil.getZombieSpawn (zombieData));
        }
    }

    /**
     * Will load all the player spawn points saved into the flat file for spawns
     */
    private void loadPlayerSpawns (){
        final List<String> playerList = TurfZ.getConfigRegistry ().getSpawnConfig ().getStringList ("players");

        for (final String playerSpawnData : playerList){
            playerSpawns.add (LocationUtil.getPlayerSpawn (playerSpawnData));
        }
    }

    /**
     *
     * @return a location inside the radius of a randomly selected player spawn
     */
    public Location getRandomPlayerSpawn (){
        Random random = new Random ();
        final PlayerSpawn playerSpawn = playerSpawns.get (random.nextInt (playerSpawns.size ()));

        final Location location = LocationUtil.getLocationInsideRadius (playerSpawn.getLocation (), playerSpawn.getRadius ());
        return LocationUtil.getSafeSpawnLocationOver (location);
    }

    /**
     *
     * @return the list of all the spawns spawns
     */
    public List<ZombieSpawn> getZombieSpawns (){
        return zombieSpawns;
    }

    /**
     *
     * @return the list of all the player spawns
     */
    public List<PlayerSpawn> getPlayerSpawns (){
        return playerSpawns;
    }
}
