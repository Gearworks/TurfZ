package org.turfwars.turfz.persistence.locations;

import org.bukkit.Location;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.locations.player.PlayerSpawn;
import org.turfwars.turfz.persistence.locations.zombie.ZombieSpawn;
import org.turfwars.turfz.utilities.LocationUtil;
import org.turfwars.turfz.utilities.Messaging;

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
        try{
            loadZombieSpawns ();
            loadPlayerSpawns ();
        }catch (SQLException e){
            e.printStackTrace ();
        }
    }

    /**
     * Will load all the zombie spawn points saved into the MySQL table and convert them into zombie spawn objects that will be stored inside the map
     *
     * @throws SQLException
     */
    private void loadZombieSpawns () throws SQLException {
        final Connection conn = TurfZ.getDatabaseManager ().getConnection ();

        if (conn != null){
            final PreparedStatement ps = conn.prepareStatement ("SELECT * FROM zombiespawns");
            ResultSet rs = ps.executeQuery ();

            while (rs.next ()){
                zombieSpawns.add (new ZombieSpawn (new Location (TurfZ.getMainWorld (), rs.getDouble ("x"), rs.getDouble ("y"), rs.getDouble ("z")), rs.getInt ("radius")));
            }
        }
    }

    /**
     * Will load all the player spawn points saved into the MySQL table and convert them into zombie spawn objects that will be stored inside the map
     *
     * @throws SQLException
     */
    private void loadPlayerSpawns () throws SQLException{
        final Connection conn = TurfZ.getDatabaseManager ().getConnection ();

        if (conn != null){
            final PreparedStatement ps = conn.prepareStatement ("SELECT * FROM playerspawns");
            ResultSet rs = ps.executeQuery ();

            while (rs.next ()){
                playerSpawns.add (new PlayerSpawn (rs.getString ("name"), new Location (TurfZ.getMainWorld (),
                        rs.getDouble ("x"), rs.getDouble ("y"), rs.getDouble ("z"), rs.getFloat ("pitch"), rs.getFloat ("yaw")), rs.getInt ("radius")));
            }
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
     * @return the list of all the zombie spawns
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
