package org.turfwars.turfz.tasks.effects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.turfwars.turfz.TurfZ;

public class BleedTask implements Runnable {

    private final Entity entity;
    private BukkitTask task;
    private final int length;
    private int currentTime;

    public BleedTask (final Entity entity, final int length){
        this.entity = entity;
        this.length = length;
        this.currentTime = 0;
    }

    public synchronized void start (){
        task = Bukkit.getScheduler ().runTaskTimer (TurfZ.getInstance (), this, 0L, 40L);
    }

    /**
     * Will play an effect every two seconds.  In order to make sure that the bleed effect doesn't last longer than it's supposed to
     * we will set the current time 2 above it's current value to represent two seconds per tick
     */
    public synchronized void run (){
        if (((Player) entity).isOnline () && currentTime <= length){
            entity.getLocation ().getWorld ().playEffect (entity.getLocation (), Effect.STEP_SOUND, 152);
            //entity.getLocation ().getWorld ().playEffect (entity.getLocation (), Effect.STEP_SOUND, 152);
            ((Player) entity).damage (1.0D);
            currentTime += 2;
        }else{
            TurfZ.getPlayerRegistry ().getPlayer ((Player) entity).setBleedingStatus (false);
            Bukkit.getScheduler ().cancelTask (task.getTaskId ());
        }
    }
}
