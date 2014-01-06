package org.turfwars.turfz.persistence.chests;

import org.bukkit.Location;

public class LocalChest {

    private final Location location;
    private final ChestTier tier;

    /**
     * This class makes it so I don't have to iterate a hashmap :P
     *
     * @param location
     * @param tier
     */
    public LocalChest (final Location location, final ChestTier tier){
        this.location = location;
        this.tier = tier;
    }

    public Location getLocation (){
        return location;
    }

    public ChestTier getChestTier (){
        return tier;
    }
}
