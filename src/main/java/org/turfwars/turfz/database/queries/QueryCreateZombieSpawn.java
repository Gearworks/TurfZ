package org.turfwars.turfz.database.queries;

import org.turfwars.turfz.persistence.locations.zombie.ZombieSpawn;

public class QueryCreateZombieSpawn implements Query{

    private final ZombieSpawn zombieSpawn;

    public QueryCreateZombieSpawn (final ZombieSpawn zombieSpawn){
        this.zombieSpawn = zombieSpawn;
    }

    public String [] getQuery (){
        String query = "INSERT INTO zombiespawns (ID, x, y, z, radius) VALUES (NULL, %d, %d, %d, %d)";

        return new String [] { String.format (query, (int) zombieSpawn.getLocation ().getX (),
                                                     (int) zombieSpawn.getLocation ().getY (),
                                                     (int) zombieSpawn.getLocation ().getZ (),
                                                           zombieSpawn.getRaidus ())};
    }
}
