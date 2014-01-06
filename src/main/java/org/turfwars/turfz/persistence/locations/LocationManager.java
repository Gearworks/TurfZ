package org.turfwars.turfz.persistence.locations;

import org.bukkit.Location;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.locations.zombie.ZombieSpawn;
import org.turfwars.turfz.utilities.Messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationManager {

    private final List<ZombieSpawn> zombieSpawns = new ArrayList<ZombieSpawn> ();

    public LocationManager (){
        try{
            loadZombieSpawns ();
        }catch (SQLException e){
            e.printStackTrace ();
        }
    }

    /**
     * Will load all the spawn points saved into the MySQL table and convert them into zombie spawn objects that will be stored inside the map
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
     *
     * @return the list of all the zombie spawns
     */
    public List<ZombieSpawn> getZombieSpawns (){
        return zombieSpawns;
    }
}
