package org.turfwars.turfz.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.locations.zombie.ZombieSpawn;
import org.turfwars.turfz.utilities.LocationUtil;
import org.turfwars.turfz.utilities.Messaging;

public class SpawningTask implements Runnable {

    private int maxZombies;
    private int zombieCount;

    public SpawningTask (){
        maxZombies = 20; //TurfZ.getConfigRegistry ().getZombieConfig ().getInt ("maxzombies");
        zombieCount = 0;
    }

    /**
     * Task will be called to attempt and spawn a zombie for each spawn point
     */
    public void run (){
        for (final ZombieSpawn zombieSpawn : TurfZ.getLocationManager ().getZombieSpawns ()){
            // Get a spawn inside the radius of the set location
            final Location location = LocationUtil.getLocationInsideRadius (zombieSpawn.getLocation (), zombieSpawn.getRaidus ());
            // Make sure that location is a safe spot to spawn
            Location locToSpawn = LocationUtil.getSafeSpawnLocationOver (location);

            // Spawn world in the given location and also give the zombie added speed for an increase in difficulty
            if (locToSpawn != null && zombieCount <= maxZombies){
                // TODO - make a chance for pig zombie to spawn instead (with increased stats)?
                final Entity entity = TurfZ.getMainWorld ().spawnEntity (locToSpawn, EntityType.ZOMBIE);
                ((LivingEntity) entity).addPotionEffect (new PotionEffect (PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            }
        }
    }
}
