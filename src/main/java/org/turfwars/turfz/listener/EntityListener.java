package org.turfwars.turfz.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.utilities.Messaging;

public class EntityListener implements Listener {

    /**
     * Constructor will register the listener
     */
    public EntityListener (){
        Bukkit.getPluginManager ().registerEvents (this, TurfZ.getInstance ());
    }

    /**
     * Will make sure that only zombies/pig zombies will spawn
     *
     * @param event
     */
    @EventHandler
    public void onCreatureSpawn (final CreatureSpawnEvent event){
        if (event.getEntityType () != EntityType.ZOMBIE){
            event.setCancelled (true);
            return;
        }
    }

    /**
     * Stop zombies from burning in the sunlight
     *
     * @param event
     */
    @EventHandler
    public void onEntityCombust (final EntityCombustEvent event){
        if (event.getEntityType () == EntityType.ZOMBIE){
            event.setCancelled (true);
        }
    }

    /**
     * Will stop any entity from dropping exp
     *
     * @param event
     */
    @EventHandler
    public void onExpDrop (final EntityDeathEvent event){
        event.setDroppedExp (0);
    }


}
