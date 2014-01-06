package org.turfwars.turfz.database.queries;

import org.bukkit.Location;

public class QueryCreateChest implements Query {

    private final Location location;
    private final int radius;
    private final String tier;

    public QueryCreateChest (final Location location, final int radius, final String tier){
        this.location = location;
        this.radius = radius;
        this.tier = tier;
    }

    public String [] getQuery (){
        String query = "INSERT INTO chests (ID, x, y, z, radius, tier) VALUES (NULL, %d, %d, %d, %d, '%s')";

        return new String [] {String.format (query, location.getBlockX (), location.getBlockY (), location.getBlockZ (), radius, tier)};
    }
}
