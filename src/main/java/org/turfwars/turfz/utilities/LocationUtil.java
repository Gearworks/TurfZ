package org.turfwars.turfz.utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.chests.ChestTier;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.persistence.locations.spawns.PlayerSpawn;
import org.turfwars.turfz.persistence.locations.spawns.ZombieSpawn;

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
     * @return closest available location the spawns can spawn at
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

    /**
     * @return Location object by string.
     */
    public static Location getLocation (final String coordinates) {
        final String[] chunks = coordinates.split (" ");

        final double posX = Double.parseDouble (chunks [0]);
        final double posY = Double.parseDouble (chunks [1]);
        final double posZ = Double.parseDouble (chunks [2]);

        float yaw = 0f;
        float pitch = 0f;

        if (chunks.length == 5) {
            yaw = (Float.parseFloat (chunks [3]) + 180 + 360) % 360;
            pitch = Float.parseFloat (chunks [4]);
        }

        return chunks.length == 5 ?
                new Location (TurfZ.getMainWorld (), posX, posY, posZ, yaw, pitch) :
                new Location (TurfZ.getMainWorld (), posX, posY, posZ);
    }

    /**
     * @return Zombie location object from a string
     *
     * ex string - x y z pitch yaw radius
     */
    public static ZombieSpawn getZombieSpawn (final String coordinates) {
        final String[] chunks = coordinates.split (" ");

        final double posX = Double.parseDouble (chunks [0]);
        final double posY = Double.parseDouble (chunks [1]);
        final double posZ = Double.parseDouble (chunks [2]);
        float yaw = (Float.parseFloat (chunks [3]) + 180 + 360) % 360;;
        float pitch = Float.parseFloat (chunks [4]);
        final int radius = Integer.parseInt (chunks[5]);

        return new ZombieSpawn (new Location (TurfZ.getMainWorld (), posX, posY, posZ, yaw, pitch), radius);
    }

    /**
     * @return Player location object from a string
     *
     * ex string - name x y z yaw pitch tier radius
     */
    public static PlayerSpawn getPlayerSpawn (final String coordinates) {
        final String[] chunks = coordinates.split (" ");

        final double posX = Double.parseDouble (chunks [1]);
        final double posY = Double.parseDouble (chunks [2]);
        final double posZ = Double.parseDouble (chunks [3]);
        float yaw = (Float.parseFloat (chunks [4]) + 180 + 360) % 360;;
        float pitch = Float.parseFloat (chunks [5]);
        final int radius = Integer.parseInt (chunks[6]);

        return new PlayerSpawn (chunks[0], new Location (TurfZ.getMainWorld (), posX, posY, posZ, yaw, pitch), radius);
    }

    /**
     *
     * @param coords
     * @return local chest object loaded from the config via string
     */
    public static LocalChest getLocalChest (final String coords){
        final String[] chunks = coords.split (" ");

        final double posX = Double.parseDouble (chunks [0]);
        final double posY = Double.parseDouble (chunks [1]);
        final double posZ = Double.parseDouble (chunks [2]);
        final ChestTier chestTier = TurfZ.getTierRegistry ().getChestTier (chunks [3]);

        return new LocalChest (new Location (TurfZ.getMainWorld (), posX, posY, posZ), chestTier);
    }
}
