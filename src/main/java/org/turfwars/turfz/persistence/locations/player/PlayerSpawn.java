package org.turfwars.turfz.persistence.locations.player;

import org.bukkit.Location;

public class PlayerSpawn {

    private final String name;
    private final Location location;
    private final int radius;

    public PlayerSpawn (final String name, final Location location, final int radius){
        this.name = name;
        this.location = location;
        this.radius = radius;
    }

    public Location getLocation (){
        return location;
    }

    public int getRadius (){
        return radius;
    }

    public String getName (){
        return name;
    }
}
