package org.turfwars.turfz.utilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.persistence.locations.spawns.ZombieSpawn;

import java.util.ArrayList;
import java.util.List;

// TODO make it work plz
public class ConfigUtil {

    public static void saveChestToConfig (){
        final List<String> toSave = new ArrayList<String> ();
        for (final LocalChest localChest : TurfZ.getChests ()){
            toSave.add (String.format ("%d %d %d %s", localChest.getLocation ().getBlockX (),
                                                      localChest.getLocation ().getBlockY (),
                                                      localChest.getLocation ().getBlockZ (),
                                                      localChest.getChestTier ()));
        }

        TurfZ.getConfigRegistry ().getSpawnConfig ().set ("chests", toSave);
    }

    public static void saveZombieSpawnsToConfig (){
        final List<String> toSave = new ArrayList<String> ();
        for (final ZombieSpawn zombieSpawn : TurfZ.getLocationManager ().getZombieSpawns ()){
            final String value  = String.format ("%d %d %d %f %f %d", zombieSpawn.getLocation ().getBlockX (),
                    zombieSpawn.getLocation ().getBlockY (),
                    zombieSpawn.getLocation ().getBlockZ (),
                    zombieSpawn.getLocation ().getYaw (),
                    zombieSpawn.getLocation ().getPitch (),
                    zombieSpawn.getRadius ());
            toSave.add (value);
        }

        TurfZ.getConfigRegistry ().getSpawnConfig ().set ("zombies", toSave);
        for (final String value : TurfZ.getConfigRegistry ().getSpawnConfig ().getStringList ("zombies")){
            Messaging.info (value);
        }
    }

    public static void savePlayerSpawnToConfig (final String name, final Location location, final int radius){
        TurfZ.getConfigRegistry ().getSpawnConfig ().getStringList ("spawns.players").add (String.format ("%s %d %d %d %f %f %d", name,
                                                                                                                                  location.getBlockX (),
                                                                                                                                  location.getBlockY (),
                                                                                                                                  location.getBlockZ (),
                                                                                                                                  location.getYaw (),
                                                                                                                                  location.getPitch (),
                                                                                                                                  radius));
    }
}
