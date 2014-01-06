package org.turfwars.turfz.utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Random;

public class LocationUtil {

    private static final Random random = new Random ();

    /**
     * Will check to see if the second location is
     *
     * @param loc1 given location
     * @param loc2 location to check if it's inside the radius of the first location
     * @param radius the radius in which the second location can be from
     * @return true if the second location is in the radius of the first, otherwise false
     */
    public static boolean inRadius (final Location loc1, final Location loc2, final int radius){
        if (loc2.getWorld ().equals (loc1.getWorld ())){
            double i1 = loc1.getX();
            double j1 = loc1.getY();
            double k1 = loc1.getZ();

            double i2 = loc2.getX();
            double j2 = loc2.getY();
            double k2 = loc2.getZ();

            double ad = Math.sqrt((i2 - i1)*(i2 - i1) + (j2 - j1)*(j2 - j1) + (k2 - k1)*(k2 - k1));

            if (ad < radius){
                return true;
            }
        }

        return false;
    }

    public static Location getLocationInsideRadius (final Location location, final int radius){
        final Location toReturn = location.clone ();
        toReturn.add((random.nextBoolean () ? 1 : -1) * random.nextInt (radius),
                     (random.nextBoolean () ? 1 : -1) * random.nextInt (radius),
                     (random.nextBoolean () ? 1 : -1) * random.nextInt (radius));

        // TODO - Fix it so that they can spawn at any y level as long as it isn't air
        toReturn.setY (location.getY ());
        Messaging.info (toReturn.toString ());
        return toReturn;
    }

    /**
     * Will make sure zombies don't spawn in places where they shouldn't be
     *
     * @param location
     * @return closest available location the zombie can spawn at
     */
    public static Location getSafeSpawnLocationOver(Location location) {

        ArrayList<Material> safeMaterials = new ArrayList<Material> ();
        safeMaterials.add (Material.WALL_SIGN);
        safeMaterials.add (Material.SIGN_POST);
        safeMaterials.add (Material.AIR);
        safeMaterials.add (Material.REDSTONE_WIRE);
        safeMaterials.add (Material.TORCH);
        safeMaterials.add (Material.REDSTONE_TORCH_OFF);
        safeMaterials.add (Material.REDSTONE_TORCH_ON);
        safeMaterials.add (Material.TRIPWIRE);
        safeMaterials.add (Material.TRIPWIRE_HOOK);
        safeMaterials.add (Material.WATER);

        for (int i = 1; i < 256 - location.getBlockY (); i++) {

            Location tempLoc = location.clone ().add (0, i, 0);
            Block tempBlock = tempLoc.getBlock ();
            Material tempMat = tempBlock.getType ();

            Location tempOverLoc = location.clone().add (0, i + 1, 0);
            Block tempOverBlock = tempOverLoc.getBlock ();
            Material tempOverMat = tempOverBlock.getType ();

            if (safeMaterials.contains (tempMat) && safeMaterials.contains (tempOverMat))
                return tempLoc;

        }

        return null;

    }
}
